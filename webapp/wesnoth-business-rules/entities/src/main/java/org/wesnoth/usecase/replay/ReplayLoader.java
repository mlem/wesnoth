package org.wesnoth.usecase.replay;

import org.wesnoth.gateway.replays.Replay;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ReplayLoader extends Observable {
    List<String> list = new ArrayList<>();

    public void addLine(String line) {
        list.add(line);
    }

    public void loadingFinished(long elapsedTime) {
        setChanged();
        notifyObservers(Event.LOADED);
        clearChanged();
    }

    public void registerObserver(Observer observer) {
        addObserver(observer);
    }

    public Replay toReplay() {

        return new Replay();
    }

    public enum Event implements org.wesnoth.usecase.replay.Event {
        LOADED;
    }
}
