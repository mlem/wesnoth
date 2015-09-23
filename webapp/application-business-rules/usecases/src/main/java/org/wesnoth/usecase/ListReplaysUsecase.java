package org.wesnoth.usecase;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListReplaysUsecase {

    private final ReplayGateway replayGateway;

    public ListReplaysUsecase(ReplayGateway replayGateway) {
        this.replayGateway = replayGateway;
    }

    public void execute(Request request, Response response) {
        try {
            response.replayInfos = replayGateway.listReplays(request.replayConnection());
        } catch (ExternalServiceException e) {
            // TODO - do something with this exception
        }
        response.success = true;

    }

    public static class Request {
        private ReplayConnection replayConnection;

        public Request(ReplayConnection replayConnection) {
            this.replayConnection = replayConnection;
        }

        public ReplayConnection replayConnection() {
            return replayConnection;
        }
    }

    public static class Response {

        private List<ReplayInfo> replayInfos;
        private boolean success;

        public Collection<ReplayInfo> foundReplays() {
            return replayInfos;
        }

        public boolean success() {
            return success;
        }
    }
}
