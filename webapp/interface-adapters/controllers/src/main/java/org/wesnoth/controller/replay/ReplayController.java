package org.wesnoth.controller.replay;

import org.springframework.stereotype.Controller;
import org.wesnoth.gateway.replays.ReplayGatewayImpl;
import org.wesnoth.usecase.ListReplaysUsecase;
import org.wesnoth.usecase.ReplayInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

@Controller
public class ReplayController {

    public static String intToARGB(int i) {
        return Integer.toHexString(((i >> 16) & 0xFF)) +
                Integer.toHexString(((i >> 8) & 0xFF)) +
                Integer.toHexString((i & 0xFF));
    }

    public List<ReplayInfoDto> showList(ListReplaysUsecase.Request request) {
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(new ReplayGatewayImpl());
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);
        Collection<ReplayInfo> replayInfos = response.foundReplays();

        return replayInfos.stream().map(replayInfo -> new ReplayInfoDto(replayInfo.getDownloadUri(),
                replayInfo.getFilename(),
                replayInfo.getRecordedDate(),
                replayInfo.getReplaySize() / 1024 + "K",
                escapeHtml4(replayInfo.getGameName()),
                replayInfo.getEra(),
                replayInfo.getPlayers().stream()
                        .map(player -> new ReplayInfoDto.PlayerDto(
                                escapeHtml4(player.getUsername()),
                                intToARGB(player.getUsername().hashCode())))
                        .collect(Collectors.toList()),
                        replayInfo.getMapName())
        ).collect(Collectors.toList());
    }
}
