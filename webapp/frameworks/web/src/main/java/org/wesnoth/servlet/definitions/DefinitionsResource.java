package org.wesnoth.servlet.definitions;

import org.plutext.jaxb.svg11.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

@Controller
@RequestMapping(value = "/definitions")
public class DefinitionsResource {

    @RequestMapping(value = "/terrain.svg", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody ResponseEntity<Svg> test(HttpServletRequest request, HttpServletResponse response) {
        ObjectFactory factory = new ObjectFactory();
        Svg svg =  factory.createSvg();

        /*
        <svg xmlns="http://www.w3.org/2000/svg" version="1.1" baseProfile="full"
       xmlns:xlink="http://www.w3.org/1999/xlink" height="0px" width="0px">
         */
        svg.setVersion("1.1");
        svg.setBaseProfile("full");
        svg.setHeight("0px");
        svg.setWidth("0px");
        List<Object> svgContent = svg.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        Title title = factory.createTitle();
        title.setContent("generated Definitions for Map Tiles");
        svgContent.add(wrapWithJAXBElement("title", title));
        Defs defs = new Defs();

//         <defs>...</defs>

        svgContent.add(wrapWithJAXBElement("defs",defs));
        List<Object> defsContent = defs.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        SVGPatternClass pattern = new SVGPatternClass();

        //       <pattern id="pattern-Wog" x="0" y="0" patternUnits="userSpaceOnUse" height="72" width="72">
        //                  <image height="72" width="72" xlink:href="/data/core/images/terrain/water/ocean-grey-tile.png"/>
        //            </pattern>

        pattern.setId("pattern-Wog");
        pattern.setHeight("72");
        pattern.setWidth("72");
        pattern.setX("0");
        pattern.setY("0");
        pattern.setPatternUnits("userSpaceOnUse");
        List<Object> patternContent = pattern.getSVGDescriptionClassOrSVGAnimationClassOrSVGStructureClass();
        SVGImageClass image = new SVGImageClass();
        image.setHeight("72");
        image.setWidth("72");
        image.setHref("/data/core/images/terrain/water/ocean-grey-tile.png");
        patternContent.add(wrapWithJAXBElement("image",image));
        defsContent.add(wrapWithJAXBElement("pattern",pattern));
        Polygon polygon = new Polygon();

        //  <polygon id="Wog" class="hex" points="18,0 54,0 72,36 54,72 18,72 0,36" fill="url(#pattern-Wog)"/>

        polygon.setId("Wog");
        polygon.setClazz("hex");
        polygon.setPoints("18,0 54,0 72,36 54,72 18,72 0,36");
        polygon.setFill("url(#pattern-Wog)");
        defsContent.add(wrapWithJAXBElement("polygon", polygon));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        ResponseEntity<Svg> svgEntity = new ResponseEntity<Svg>(svg, headers, HttpStatus.OK);
        return svgEntity;
    }

    private <T> JAXBElement<T> wrapWithJAXBElement(String tagName, T element) {
        QName tagQName = new QName(tagName);
        return new JAXBElement<T>(tagQName, (Class<T>) element.getClass(), element);
    }
}
