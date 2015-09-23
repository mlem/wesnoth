package org.wesnoth.usecase;

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
    private final List<String> players;

    public ReplayInfo(URI downloadUri, String filename, Date recordedDate, int replaySize, String gameName, String era, List<String> players) {
        this.downloadUri = downloadUri;
        this.filename = filename;
        this.recordedDate = recordedDate;
        this.replaySize = replaySize;
        this.gameName = gameName;
        this.era = era;
        this.players = players;
    }
}
