package org.wesnoth.connection.replays;

import org.wesnoth.connection.ExternalServiceException;

import java.io.InputStream;

public interface ReplayConnection {
    InputStream connect() throws ExternalServiceException;

    String currentUrl();
}
