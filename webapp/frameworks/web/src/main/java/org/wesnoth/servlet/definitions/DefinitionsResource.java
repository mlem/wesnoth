package org.wesnoth.servlet.definitions;

import org.plutext.jaxb.svg11.Svg;
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

@Controller
@RequestMapping(value = "/definitions")
public class DefinitionsResource {

    @RequestMapping(value = "/terrain.svg", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody ResponseEntity<Svg> test(HttpServletRequest request, HttpServletResponse response) {

        Svg svg = createSvgBuilder()
                    .addTitle()
                        .content("generated Definitions for Map Tiles")
                    .finish()
                    .addDefs()
                        .addPattern()
                            .id("pattern-Wog")
                            .x("0")
                            .y("0")
                            .patternUnits("userSpaceOnUse")
                            .height("72")
                            .width("72")
                            .addImage()
                                .height("72")
                                .width("72")
                                .href("/data/core/images/terrain/water/ocean-grey-tile.png")
                            .finish()
                        .finish()
                        .addPolygon()
                            .id("Wog")
                            .clazz("hex")
                            .points("18,0 54,0 72,36 54,72 18,72 0,36")
                            .fill("url(#pattern-Wog)")
                        .finish()
                    .finish()
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        ResponseEntity<Svg> svgEntity = new ResponseEntity<Svg>(svg, headers, HttpStatus.OK);
        return svgEntity;
    }

    private SvgBuilder createSvgBuilder() {
        SvgBuilder svgBuilder = new SvgBuilder();
        return svgBuilder;
        /*
        <svg xmlns="http://www.w3.org/2000/svg" version="1.1" baseProfile="full"
       xmlns:xlink="http://www.w3.org/1999/xlink" height="0px" width="0px">
         */
    }

    private <T> JAXBElement<T> wrapWithJAXBElement(String tagName, T element) {
        QName tagQName = new QName(tagName);
        return new JAXBElement<T>(tagQName, (Class<T>) element.getClass(), element);
    }
}
