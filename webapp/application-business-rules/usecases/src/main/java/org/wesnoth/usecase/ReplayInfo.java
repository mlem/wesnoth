package org.wesnoth.usecase;

import org.wesnoth.UserName;
import org.wesnoth.gateway.replays.Compression;

import java.net.URI;
import java.util.Date;
import java.util.List;

public class ReplayInfo {
    private final URI downloadUri;
    private final String filename;
    private final Date recordedDate;
    private final int replaySize;
    private final String gameName;
    private final String era;
    private final List<UserName> players;
    private String mapName;
    private final Integer replayId;
    private final Compression compression;

    public ReplayInfo(URI downloadUri, String filename, Date recordedDate, int replaySize, String gameName, String era, List<UserName> players, String mapName, Integer replayId, Compression compression) {
        this.downloadUri = downloadUri;
        this.filename = filename;
        this.recordedDate = recordedDate;
        this.replaySize = replaySize;
        this.gameName = gameName;
        this.era = era;
        this.players = players;
        this.mapName = mapName;
        this.replayId = replayId;
        this.compression = compression;
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

    public int getReplaySize() {
        return replaySize;
    }

    public String getGameName() {
        return gameName;
    }

    public String getEra() {
        return era;
    }

    public List<UserName> getPlayers() {
        return players;
    }

    public String getMapName() {
        return mapName;
    }

    public Integer getReplayId() {
        return replayId;
    }

    public Compression getCompression() {
        return compression;
    }
}
