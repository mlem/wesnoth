package org.wesnoth.servlet.replay;

import java.net.URI;

public class ImageDto {
    private final String name;
    private final URI uri;

    public ImageDto(String name, URI uri) {

        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public URI getUri() {
        return uri;
    }
}
