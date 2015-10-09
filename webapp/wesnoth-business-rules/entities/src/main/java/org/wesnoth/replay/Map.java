package org.wesnoth.replay;

import org.wesnoth.Tile;

import java.util.ArrayList;
import java.util.List;

public class Map implements org.wesnoth.Event {
    private String map;

    public Map(String map) {
        this.map = map;
    }
    public List<List<Tile>> getMap() {
        List<List<Tile>> map = new ArrayList<>();
        for(String row : this.map.split("\n")) {
            List<Tile> rowList = new ArrayList<>();
            map.add(rowList);
            for(String tileString : row.split(",")) {
                rowList.add(new Tile(tileString.trim()));
            }
        }
        return map;
    }

    @Override
    public String toString() {
        return map;
    }
}
