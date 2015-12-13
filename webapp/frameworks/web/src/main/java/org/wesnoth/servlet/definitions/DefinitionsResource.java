package org.wesnoth.servlet.definitions;

import org.plutext.jaxb.svg11.Svg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wesnoth.servlet.replay.ImageController;
import org.wesnoth.servlet.replay.ImageDto;
import org.wesnoth.usecase.images.ImageMappingUsecase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/definitions")
public class DefinitionsResource {

    private ImageController imageController;

    @Autowired
    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }

    @RequestMapping(value = "/terrain.svg", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody ResponseEntity<Svg> terrain(HttpServletRequest request, HttpServletResponse response) {

        getClass().getResourceAsStream("terrain.cfg");
        List<ImageDto> imageDtos = imageController.listImages(
                new ImageMappingUsecase.Request(getClass().getResourceAsStream("/data/core/terrain.cfg"))
        );

        SvgBuilder.SvgDefsBuilder defs = createDefinitionsBuilder();
        imageDtos.stream().forEach(imageDto -> {
            addTile(defs, imageDto.getUri().toString(), imageDto.getName());
        });
        Svg svg = defs.finish().build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return new ResponseEntity<>(svg, headers, HttpStatus.OK);
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
