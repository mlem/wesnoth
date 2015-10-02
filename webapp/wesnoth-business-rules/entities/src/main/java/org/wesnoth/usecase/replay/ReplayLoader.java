package org.wesnoth.usecase.replay;

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
        notifyObservers("finished loading after " + elapsedTime + " milliseconds.");
        clearChanged();
    }

    public void registerObserver(Observer observer) {
        addObserver(observer);
    }
}
