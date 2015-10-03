package org.wesnoth.gateway.replays;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.replay.Replay;
import org.wesnoth.replay.ReplayLoader;
import org.wesnoth.replay.ReplayMeta;

import java.util.List;

public interface ReplayGateway {

    List<ReplayMeta> listReplays(ReplayConnection replayConnection) throws ExternalServiceException;

    Replay loadReplay(ReplayLoader replayLoader, ReplayConnection replayConnection) throws ExternalServiceException;
}
