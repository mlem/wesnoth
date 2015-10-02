package org.wesnoth.usecase.replay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewReplayUsecase {

    private final Request request;
    private final Response response;

    public ViewReplayUsecase(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public void execute() {

        Replay replay = new Replay();
        replay.registerObserver(request.observer);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.stream));
        new Thread(() -> {
        String line = null;
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                replay.addLine(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            response.exception = e;
            response.success = false;
        }
        }).start();
    }

    public static class Request {

        private final InputStream stream;
        private final Observer observer;

        public Request(InputStream stream, Observer observer) {
            this.stream = stream;
            this.observer = observer;
        }
    }

    public static class Response {
        public Exception exception;
        public boolean success;
    }

    private class Replay extends Observable{
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
