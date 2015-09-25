package org.wesnoth.controller;

import org.springframework.stereotype.Controller;
import org.wesnoth.UserName;
import org.wesnoth.gateway.replays.ReplayGatewayImpl;
import org.wesnoth.usecase.ListReplaysUsecase;
import org.wesnoth.usecase.ReplayInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReplayController {

    public List<ReplayInfoDto> showList(ListReplaysUsecase.Request request) {
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(new ReplayGatewayImpl());
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);
        Collection<ReplayInfo> replayInfos = response.foundReplays();

        return replayInfos.stream().map(replayInfo -> new ReplayInfoDto(replayInfo.getDownloadUri(),
                replayInfo.getFilename(),
                replayInfo.getRecordedDate(),
                replayInfo.getReplaySize() / 1024 + "K",
                replayInfo.getGameName(),
                replayInfo.getEra(),
                replayInfo.getPlayers().stream().map(UserName::getUsername).collect(Collectors.toList()))).collect(Collectors.toList());
    }
}
