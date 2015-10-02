package org.wesnoth.gateway.replays;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.ReplayMeta;
import org.wesnoth.usecase.replay.ViewReplayUsecase;

import java.util.List;

public interface ReplayGateway {

    List<ReplayMeta> listReplays(ReplayConnection replayConnection) throws ExternalServiceException;

    void loadReplay(ViewReplayUsecase.ReplayViewer replayViewer, ReplayConnection replayConnection) throws ExternalServiceException;
}
