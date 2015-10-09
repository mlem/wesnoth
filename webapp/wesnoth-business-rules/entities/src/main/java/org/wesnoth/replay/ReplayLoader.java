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
        String line;
        while (iterator.hasNext()) {
            line = iterator.next();
            if (line.contains("=")) {
                String key = line.split("=")[0];
                String value = line.split("=")[1];
                if(value.startsWith("\"") && !value.trim().equals("\"\"")) {
                    while (iterator.hasNext() && (!value.endsWith("\"") || value.endsWith("\"\""))) {
                        value+= "\n" + iterator.next().trim();
                    }
                }
                root.addAttribute(key, value.replace("\"", ""));
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
