package org.wesnoth.controller;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class ReplayInfoDto {
    private final URI downloadUri;
    private final String filename;
    private final Date recordedDate;
    private final String replaySize;
    private final String gameName;
    private final String era;
    private final List<PlayerDto> players;

    public ReplayInfoDto(URI downloadUri, String filename, Date recordedDate, String replaySize, String gameName, String era, List<PlayerDto> players) {
        this.downloadUri = downloadUri;
        this.filename = filename;
        this.recordedDate = recordedDate;
        this.replaySize = replaySize;
        this.gameName = gameName;
        this.era = era;
        this.players = players;
    }

    public URI getDownloadUri() {
        return downloadUri;
    }

    public String getFilename() {
        return filename;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public String getReplaySize() {
        return replaySize;
    }

    public String getGameName() {
        return gameName;
    }

    public String getEra() {
        return era;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public static class PlayerDto {

        private final String name;
        private final String colorCode;

        public PlayerDto(String name, String colorCode) {

            this.name = name;
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return colorCode;
        }

        public String getName() {
            return name;
        }
    }
}
