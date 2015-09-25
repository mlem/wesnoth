package org.wesnoth.controller;

import org.springframework.stereotype.Controller;
import org.wesnoth.gateway.replays.ReplayGatewayImpl;
import org.wesnoth.usecase.ListReplaysUsecase;
import org.wesnoth.usecase.ReplayInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReplayController {

    public List<ReplayDto> showList(ListReplaysUsecase.Request request) {
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(new ReplayGatewayImpl());
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);
        Collection<ReplayInfo> replayInfos = response.foundReplays();
        List<ReplayDto> replayDtos = replayInfos.stream().map(replayInfo -> {
            return new ReplayDto(replayInfo.getDownloadUri().getRawPath(),
                    replayInfo.getGameName(),
                    replayInfo.getEra(),
                    replayInfo.getPlayers().stream().map(user -> user.getUsername()).collect(Collectors.toList()));
        }).collect(Collectors.toList());

        return replayDtos;
    }
}
