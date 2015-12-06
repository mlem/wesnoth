package org.wesnoth.usecase.images;

import org.wesnoth.replay.WmlConverter;
import org.wesnoth.wml.WMLTag;

import java.io.InputStream;

public class ConfigGateway {

    private InputStream connection;

    public ConfigGateway(InputStream connection) {
        this.connection = connection;
    }

    public WMLTag toWmlTag() {
        return new WmlConverter().convert(connection);
    }
}
