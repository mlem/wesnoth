package org.wesnoth.gateway.replays;

import java.util.stream.Stream;

public enum Compression {
    GZIP("gz"),
    BZIP("bz"),
    UNKNOWN("");

    private String fileExtension;

    Compression(String fileExtension) {

        this.fileExtension = fileExtension;
    }

    public static Compression findBy(String fileExtension) {
        return Stream.of(values()).filter(compression -> compression.fileExtension.equals(fileExtension)).findFirst().orElse(UNKNOWN);
    }
}
