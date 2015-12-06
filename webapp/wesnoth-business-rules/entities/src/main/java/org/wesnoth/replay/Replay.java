package org.wesnoth.replay;

import org.wesnoth.wml.WMLTag;

import java.util.Observable;
import java.util.Observer;

public class Replay extends Observable {

    private WMLTag wmlTag;

    public Replay(WMLTag wmlTag) {

        this.wmlTag = wmlTag;
    }

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

    public CampaignType getCampaignType() {
        return Replay.CampaignType.valueOf(wmlTag.getAttribute("campaign_type").toUpperCase());
    }


    public Difficulty getDifficulty() {
        return Replay.Difficulty.valueOf(wmlTag.getAttribute("difficulty").toUpperCase());
    }


    public String getLabel() {
        return wmlTag.getAttribute("label");
    }

    public String getGameTitle() {
        return wmlTag.getAttribute("mp_game_title");
    }

    public Integer getRandomSeed() {
        return Integer.parseInt(wmlTag.getAttribute("random_seed"));
    }

    public String getVersion() {
        return wmlTag.getAttribute("version");
    }

    public void displayMap() {
        wmlTag.findTag("replay_start").ifPresent(
                tag -> {
                    setChanged();
                    String mapData = tag.getAttribute("map_data");
                    String map = mapData.substring(mapData.indexOf("\n\n")+2);
                    String parsedMap = map.replaceAll("\\_off\\^\\_usr", "off_usr").replaceAll("\\d+ ", "");
                    notifyObservers(new Map(parsedMap));
                    clearChanged();
                }
        );
    }

    public enum Event implements org.wesnoth.Event {
        FINISH;
    }

    public enum Difficulty {
        NORMAL;
    }
    public enum CampaignType {
        MULTIPLAYER;
    }

}
