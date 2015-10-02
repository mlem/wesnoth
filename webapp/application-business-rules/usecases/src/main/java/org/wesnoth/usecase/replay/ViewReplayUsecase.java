package org.wesnoth.usecase.replay;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewReplayUsecase {

    private final ReplayGateway replayGateway;

    public ViewReplayUsecase(ReplayGateway replayGateway) {
        this.replayGateway = replayGateway;
    }

    public void execute(Request request, Response response) {

        ReplayViewer replayViewer = new ReplayViewer();
        replayViewer.registerObserver(request.observer);

        try {
            replayGateway.loadReplay(replayViewer, request.replayConnection);
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
        public Exception exception;
        public boolean success;
    }

    public static class ReplayViewer extends Observable {
        List list = new ArrayList<String>();
        public void addLine(String line) {
            list.add(line);
            setChanged();
            notifyObservers(line);
            clearChanged();
        }

        public void registerObserver(Observer observer) {
            addObserver(observer);
        }
    }
}
