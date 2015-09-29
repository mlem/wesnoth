package org.wesnoth.servlet.replay.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wesnoth.controller.replay.ReplayController;
import org.wesnoth.controller.replay.ReplayInfoDto;

@Controller
@RequestMapping(value = "/replay/view")
public class ViewResource {

    @Autowired
    private ReplayController replayController;

    @RequestMapping(value = "/{replayId}", method = RequestMethod.GET)
    public ModelAndView listReplays(@PathVariable(value="replayId") Integer replayId) {


        ReplayInfoDto replayInfoDto = new ReplayInfoDto(null, null, null, null, null, null, null, null, replayId);
        return new ModelAndView("viewReplay", "replayInfo", replayInfoDto);
    }



}
