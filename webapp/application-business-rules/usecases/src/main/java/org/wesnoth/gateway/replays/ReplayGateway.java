package org.wesnoth.gateway.replays;

import org.wesnoth.ReplayMeta;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.replay.ReplayLoader;

import java.util.List;

public interface ReplayGateway {

    List<ReplayMeta> listReplays(ReplayConnection replayConnection) throws ExternalServiceException;

    Replay loadReplay(ReplayLoader replayLoader, ReplayConnection replayConnection) throws ExternalServiceException;
}
