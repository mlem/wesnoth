package org.wesnoth.gateway.replays;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.ReplayInfo;

import java.util.List;

public interface ReplayGateway {

    List<ReplayInfo> listReplays(ReplayConnection replayConnection) throws ExternalServiceException;
}
