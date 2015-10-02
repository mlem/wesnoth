package org.wesnoth.servlet.replay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wesnoth.controller.replay.ReplayController;
import org.wesnoth.controller.replay.ReplayInfoDto;
import org.wesnoth.network.connection.ReplayHttpConnection;
import org.wesnoth.usecase.replay.ListReplaysUsecase;

import java.util.List;

@Controller
@RequestMapping(value = "/replay")
public class ReplayResource {

    @Autowired
    private ReplayController replayController;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listReplays() {

        List<ReplayInfoDto> replayInfoDtos = replayController.showList(new ListReplaysUsecase.Request(new ReplayHttpConnection("/1.12/20150921/")));

        return new ModelAndView("replays", "replayInfos", replayInfoDtos);
    }
}
