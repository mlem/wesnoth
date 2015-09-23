package org.wesnoth.network.connection;

import org.junit.Test;
import org.wesnoth.connection.ExternalServiceException;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReplayHttpConnectionTest {

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Test
    public void test() throws ExternalServiceException {
        ReplayHttpConnection replayHttpConnection = new ReplayHttpConnection("/1.12/20150921/");
        InputStream inputStream = replayHttpConnection.connectAndExecute();
        String result = convertStreamToString(inputStream);

        String expected = convertStreamToString(getClass().getResourceAsStream("replays_1.12_20150921.html"));

        assertThat(result, is(expected));
    }

}