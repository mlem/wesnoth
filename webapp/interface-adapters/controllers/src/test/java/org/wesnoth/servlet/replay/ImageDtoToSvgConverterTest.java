package org.wesnoth.servlet.replay;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.plutext.jaxb.svg11.*;

import javax.xml.bind.JAXBElement;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ImageDtoToSvgConverterTest {

    private ImageDtoToSvgConverter imageDtoToSvgConverter;
    private ArrayList<ImageDto> imageDtos;

    @Before
    public void setUp() throws Exception {
        imageDtoToSvgConverter = new ImageDtoToSvgConverter();
        imageDtos = new ArrayList<>();
        imageDtos.add(new ImageDto("Wog", URI.create("/data/core/images/terrain/water/ocean-grey-tile.png")));
    }

    @Test
    public void testSvgAttributes() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);
        assertThat(svg.getWidth(), is("0px"));
        assertThat(svg.getHeight(), is("0px"));
        assertThat(svg.getBaseProfile(), is("full"));
        assertThat(svg.getVersion(), is("1.1"));
    }


    @Test
    public void testSvgElements() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);

        List<Object> elements = svg.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        assertThat(elements.size(), is(2));
    }

    @Test
    public void testTitle() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);

        JAXBElement<Title> jaxBTitle = title(svg);
        assertThat(jaxBTitle.getDeclaredType(), CoreMatchers.equalTo(Title.class));
        assertThat(jaxBTitle.getName().getLocalPart(), is("title"));
        assertThat(jaxBTitle.getValue(), is(CoreMatchers.instanceOf(Title.class)));

        Title title = jaxBTitle.getValue();
        assertThat(title.getContent(), is("generated Definitions for Map Tiles"));
    }

    private JAXBElement<Title> title(Svg svg) {
        List<Object> elements = svg.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        return (JAXBElement<Title>) elements.get(0);
    }

    @Test
    public void testDefs() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);

        JAXBElement<Defs> jaxBDefs = jaxBDefs(svg);
        assertThat(jaxBDefs.getDeclaredType(), CoreMatchers.equalTo(Defs.class));
        assertThat(jaxBDefs.getName().getLocalPart(), is("defs"));
        assertThat(jaxBDefs.getValue(), is(CoreMatchers.instanceOf(Defs.class)));

        Defs defs = jaxBDefs.getValue();
        List<Object> definitions = defs.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        assertThat(definitions.size(), is(2));
    }

    @Test
    public void testPattern() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);

        List<Object> definitions = definitions(svg);

        //       <pattern id="pattern-Wog" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
        //                  <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ocean-grey-tile.png"/>
        //            </pattern>

        assertThat(definitions.get(0), is(CoreMatchers.instanceOf(JAXBElement.class)));
        JAXBElement<SVGPatternClass> jaxBPattern = (JAXBElement<SVGPatternClass>) definitions.get(0);
        assertThat(jaxBPattern.getDeclaredType(), CoreMatchers.equalTo(SVGPatternClass.class));
        assertThat(jaxBPattern.getName().getLocalPart(), is("pattern"));
        assertThat(jaxBPattern.getValue(), is(CoreMatchers.instanceOf(SVGPatternClass.class)));

        SVGPatternClass pattern = jaxBPattern.getValue();
        assertThat(pattern.getX(), is("0"));
        assertThat(pattern.getY(), is("0"));
        assertThat(pattern.getHeight(), is("72"));
        assertThat(pattern.getWidth(), is("72"));
        assertThat(pattern.getPatternUnits(), is("userSpaceOnUse"));

        List<Object> patternContent = pattern.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        JAXBElement<SVGImageClass> jaxBImage = (JAXBElement<SVGImageClass>) patternContent.get(0);
        assertThat(jaxBImage.getDeclaredType(), CoreMatchers.equalTo(SVGImageClass.class));
        assertThat(jaxBImage.getName().getLocalPart(), is("image"));
        assertThat(jaxBImage.getValue(), is(CoreMatchers.instanceOf(SVGImageClass.class)));

        SVGImageClass image = jaxBImage.getValue();
        assertThat(image.getHeight(), is("72"));
        assertThat(image.getWidth(), is("72"));
        assertThat(image.getHref(), is("/data/core/images/terrain/water/ocean-grey-tile.png"));

    }

    @Test
    public void testPolygon() {
        Svg svg = imageDtoToSvgConverter.convertToSvg(imageDtos);

        List<Object> definitions = definitions(svg);

        //  <polygon id="Wog" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wog)"/>

        assertThat(definitions.get(1), is(CoreMatchers.instanceOf(JAXBElement.class)));
        JAXBElement<Polygon> jaxBPolygon = (JAXBElement<Polygon>) definitions.get(1);
        assertThat(jaxBPolygon.getDeclaredType(), CoreMatchers.equalTo(Polygon.class));
        assertThat(jaxBPolygon.getName().getLocalPart(), is("polygon"));
        assertThat(jaxBPolygon.getValue(), is(CoreMatchers.instanceOf(Polygon.class)));

        Polygon polygon = jaxBPolygon.getValue();
        assertThat(polygon.getId(), is("Wog"));
        assertThat(polygon.getClazz(), is("hex"));
        assertThat(polygon.getPoints(), is("18,0 54,0 72,36 54,72 18,72 0,36"));
        assertThat(polygon.getFill(), is("url(#pattern-Wog)"));
    }

    private List<Object> definitions(Svg svg) {
        JAXBElement<Defs> jaxBDefs = jaxBDefs(svg);

        Defs defs = jaxBDefs.getValue();
        return defs.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
    }

    private JAXBElement<Defs> jaxBDefs(Svg svg) {
        List<Object> elements = svg.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        return (JAXBElement<Defs>) elements.get(1);
    }

}