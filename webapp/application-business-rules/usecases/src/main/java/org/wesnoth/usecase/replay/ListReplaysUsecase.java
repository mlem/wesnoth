package org.wesnoth.usecase.replay;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;
import org.wesnoth.replay.ReplayMeta;

import java.util.Collection;
import java.util.List;

public class ListReplaysUsecase {

    private final ReplayGateway replayGateway;

    public ListReplaysUsecase(ReplayGateway replayGateway) {
        this.replayGateway = replayGateway;
    }

    public void execute(Request request, Response response) {
        try {
            response.replayMetas = replayGateway.listReplays(request.replayConnection());
            response.success = true;
        } catch (ExternalServiceException e) {
            response.exception = e;
            response.success = false;
        }

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

        public Exception exception;
        private List<ReplayMeta> replayMetas;
        private boolean success;

        public Collection<ReplayMeta> foundReplays() {
            return replayMetas;
        }

        public boolean success() {
            return success;
        }

        public Exception getException() {
            return exception;
        }
    }
}
