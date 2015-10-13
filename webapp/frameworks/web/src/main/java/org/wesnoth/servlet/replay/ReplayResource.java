package org.wesnoth.servlet.replay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.wesnoth.controller.replay.ReplayController;
import org.wesnoth.controller.replay.ReplayMetaDto;
import org.wesnoth.network.connection.ReplayHttpConnection;
import org.wesnoth.usecase.images.ImageMappingUsecase;
import org.wesnoth.usecase.replay.ListReplaysUsecase;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "/replay")
public class ReplayResource {

    @Autowired
    private ReplayController replayController;
    @Autowired
    private ImageController imageController;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listReplays() {

        List<ReplayMetaDto> replayMetaDtos = replayController.showList(new ListReplaysUsecase.Request(new ReplayHttpConnection("http://replays.wesnoth.org/1.12/20150921/")));

        return new ModelAndView("replays", "replayInfos", replayMetaDtos);
    }

    @RequestMapping(value = "/view/{replayId}", method = RequestMethod.POST)
    public ModelAndView viewReplay(@RequestParam("downloadUri") URI downloadUri) {
        List<ImageDto> images = imageController.listImages(new ImageMappingUsecase.Request(getClass().getResourceAsStream("/data/core/terrain.cfg")));

        ViewReplayDto viewReplayDto = new ViewReplayDto(downloadUri, images);
        return new ModelAndView("viewReplay", "viewReplayDto", viewReplayDto);
    }

}
