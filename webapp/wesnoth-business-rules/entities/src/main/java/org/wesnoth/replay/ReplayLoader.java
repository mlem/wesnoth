package org.wesnoth.replay;

import org.wesnoth.wml.WMLTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ReplayLoader extends Observable {

    WmlConverter converter = new WmlConverter();

    List<String> lines = new ArrayList<>();

    public void addLine(String line) {
        lines.add(line);
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
        WMLTag wmlTag = converter.convert(lines);
        return new Replay(wmlTag);
    }

    public enum Event implements org.wesnoth.Event {
        LOADED;
    }

}
