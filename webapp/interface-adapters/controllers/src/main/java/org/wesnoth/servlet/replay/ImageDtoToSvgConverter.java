package org.wesnoth.servlet.replay;

import org.plutext.jaxb.svg11.Svg;
import org.wesnoth.servlet.definitions.SvgBuilder;

import java.util.List;

public class ImageDtoToSvgConverter {

    public Svg convertToSvg(List<ImageDto> imageDtos) {
        SvgBuilder.SvgDefsBuilder defs = createDefinitionsBuilder();
        imageDtos.stream().forEach(imageDto -> {
            addTile(defs, imageDto.getUri().toString(), imageDto.getName());
        });
        return defs.finish().build();
    }



    private SvgBuilder.SvgDefsBuilder createDefinitionsBuilder() {
        return new SvgBuilder()
                .addTitle()
                .content("generated Definitions for Map Tiles")
                .finish()
                .addDefs();
    }

    private SvgBuilder.SvgDefsBuilder addTile(SvgBuilder.SvgDefsBuilder defs, String href, String id) {
        return defs
                .addPattern()
                .id("pattern-" + id)
                .x("0")
                .y("0")
                .patternUnits("userSpaceOnUse")
                .height("72")
                .width("72")
                .addImage()
                .height("72")
                .width("72")
                .href(href)
                .finish()
                .finish()
                .addPolygon()
                .id(id)
                .clazz("hex")
                .points("18,0 54,0 72,36 54,72 18,72 0,36")
                .fill("url(#pattern-"+ id+")")
                .finish();
    }
}
