package org.wesnoth.usecase.replay;

import com.google.common.base.Stopwatch;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;
import org.wesnoth.replay.Replay;
import org.wesnoth.replay.ReplayLoader;

import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class ViewReplayUsecase {

    private final ReplayGateway replayGateway;

    public ViewReplayUsecase(ReplayGateway replayGateway) {
        this.replayGateway = replayGateway;
    }

    public void execute(Request request, Response response) {

        ReplayLoader replayLoader = new ReplayLoader();
        replayLoader.registerObserver(request.observer);

        try {
            Stopwatch timer = Stopwatch.createStarted();
            Replay replay = replayGateway.loadReplay(replayLoader, request.replayConnection);
            replay.registerObserver(request.observer);
            replayLoader.loadingFinished(timer.stop().elapsed(TimeUnit.MILLISECONDS));
            replay.displayMap();
            while(!replay.isLastStep()) {
                replay.progress();
            }
            replay.finish();
            response.success = true;
        } catch (ExternalServiceException e) {
            response.success = false;
            response.exception = e;
        }
    }

    public static class Request {

        private final ReplayConnection replayConnection;
        private final Observer observer;

        public Request(ReplayConnection replayConnection, Observer observer) {
            this.replayConnection = replayConnection;
            this.observer = observer;
        }
    }

    public static class Response {
        private Exception exception;
        private boolean success;

        public Exception getException() {
            return exception;
        }

        public boolean isSuccess() {
            return success;
        }
    }

}
