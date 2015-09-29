package org.wesnoth.servlet.replay.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wesnoth.controller.replay.ReplayController;

import java.net.URI;

@Controller
@RequestMapping(value = "/replay/view")
public class ViewResource {

    @Autowired
    private ReplayController replayController;

    @RequestMapping(value = "/{replayId}", method = RequestMethod.POST)
    public ModelAndView listReplays(@PathVariable(value="replayId") Integer replayId, @RequestParam("downloadUri")URI downloadUri) {

        return new ModelAndView("viewReplay", "replayUri", downloadUri);
    }



}
