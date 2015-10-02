package org.wesnoth.network.connection;

import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ReplayHttpConnection implements ReplayConnection {
    private final String requestUri;

    public ReplayHttpConnection(String requestUri) {
        this.requestUri = requestUri;
    }

    @Override
    public InputStream connect() throws ExternalServiceException {
        InputStream inputStream;
        try {
            URL url = new URL(requestUri);
            inputStream = url.openStream();
        } catch (MalformedURLException e) {
            throw new ExternalServiceException("Couldn't fetch replays. The URL is malformed.", e);
        } catch (IOException e) {
            throw new ExternalServiceException("Couldn't fetch replays. Problem with opening Stream.", e);
        }
        return inputStream;
    }

    @Override
    public String currentUrl() {
        return requestUri;
    }
}
