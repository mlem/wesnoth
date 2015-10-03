package org.wesnoth.replay;

import org.wesnoth.wml.WMLTag;

import java.util.*;

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
        WMLTag wmlTag = convertTo("root", list.iterator());
        return new Replay(wmlTag);
    }

    public WMLTag convertTo(String tagName, Iterator<String> iterator) {
        WMLTag root = new WMLTag(tagName);
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.contains("=")) {
                root.addAttribute(line.split("=")[0], line.split("=")[1].replace("\"", ""));
            } else if (line.contains("[/") && line.contains("]")) {
                return root;
            } else if (line.contains("[") && line.contains("]")) {
                root.addTag(convertTo(line.replaceAll("\\[", "").replaceAll("\\]", ""), iterator));
            }
        }
        return root;
    }

    public enum Event implements org.wesnoth.Event {
        LOADED;
    }

}
