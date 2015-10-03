package org.wesnoth.gateway.replays;

import java.util.Observable;
import java.util.Observer;

public class Replay extends Observable {

    public boolean isLastStep() {
        return true;
    }

    public void progress() {
        setChanged();
        notifyObservers("progress");
        clearChanged();
    }

    public void registerObserver(Observer observer) {
        addObserver(observer);
    }

    public void finish() {
        setChanged();
        notifyObservers(Event.FINISH);
        clearChanged();


    }

    public enum Event implements org.wesnoth.usecase.replay.Event {
        FINISH;
    }
}
