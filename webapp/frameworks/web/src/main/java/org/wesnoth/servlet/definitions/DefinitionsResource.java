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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


        Svg svg = imageController.definitionSvg();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return new ResponseEntity<>(svg, headers, HttpStatus.OK);
    }


}
