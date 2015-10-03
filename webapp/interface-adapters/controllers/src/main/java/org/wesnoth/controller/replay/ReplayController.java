package org.wesnoth.controller.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.wesnoth.gateway.replays.ReplayGatewayImpl;
import org.wesnoth.replay.ReplayMeta;
import org.wesnoth.usecase.replay.ListReplaysUsecase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

@Controller
public class ReplayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayController.class);

    public static String intToARGB(int i) {
        return Integer.toHexString(i >> 16 & 0xFF) +
                Integer.toHexString(i >> 8 & 0xFF) +
                Integer.toHexString(i & 0xFF);
    }

    public List<ReplayMetaDto> showList(ListReplaysUsecase.Request request) {
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(new ReplayGatewayImpl());
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);
        if(!response.success()) {
            LOGGER.error("error in listReplayUsecase", response.exception);
            // TODO: don't return empty list. Return something, that the UI can handle. Maybe throw an exception.
            return new ArrayList<>();
        }
        Collection<ReplayMeta> replayMetas = response.foundReplays();

        return replayMetas.stream().map(replayInfo -> new ReplayMetaDto(replayInfo.getDownloadUri(),
                replayInfo.getFilename(),
                replayInfo.getRecordedDate(),
                replayInfo.getReplaySize() / 1024 + "K",
                escapeHtml4(replayInfo.getGameName()),
                replayInfo.getEra(),
                replayInfo.getPlayers().stream()
                        .map(player -> new ReplayMetaDto.PlayerDto(
                                escapeHtml4(player.getUsername()),
                                intToARGB(player.getUsername().hashCode())))
                        .collect(Collectors.toList()),
                        replayInfo.getMapName(),
                        replayInfo.getReplayId())
        ).collect(Collectors.toList());
    }

}
