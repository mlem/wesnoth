package org.wesnoth.connection.replays;

import org.wesnoth.connection.ExternalServiceException;

import java.io.InputStream;

public interface ReplayConnection {
    InputStream connectAndExecute() throws ExternalServiceException;

    String currentUrl();
}
