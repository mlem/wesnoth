package org.wesnoth.network.connection;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;

import java.io.IOException;
import java.io.InputStream;

public class ReplayHttpConnection implements ReplayConnection {
    private final static String HOST_URL = "replay.wesnoth.org";
    private static final String PROTOCOL = "http://";
    private final String requestUri;

    public ReplayHttpConnection(String requestUri) {
        this.requestUri = requestUri;
    }

    @Override
    public InputStream connectAndExecute() throws ExternalServiceException {
        BasicHttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
        HttpClient httpClient = new DefaultHttpClient(params);
        InputStream content = null;

        try {
            HttpHost httpHost = new HttpHost(HOST_URL);
            BasicHttpRequest request = new BasicHttpRequest("GET", this.requestUri);

            HttpResponse response = httpClient.execute(httpHost, request);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                content = response.getEntity().getContent();


            }

        } catch (IOException e) {
            throw new ExternalServiceException("Couldn't fetch replays", e);
        }
        return content;
    }

    @Override
    public String currentUrl() {
        return PROTOCOL + HOST_URL + requestUri;
    }
}
