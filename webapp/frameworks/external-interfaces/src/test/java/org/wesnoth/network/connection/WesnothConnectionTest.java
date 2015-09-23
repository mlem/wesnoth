package org.wesnoth.network.connection;

import org.junit.Ignore;
import org.junit.Test;
import org.wesnoth.UserName;

import java.io.IOException;
import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class WesnothConnectionTest {

    @Test
    @Ignore("not ready to implement yet")
    public void testConnection() throws IOException {
        WesnothConnection wesnothConnection = new WesnothConnection(InetAddress.getByName("localhost"), 15000, new UserName("BeoXTC"));

        wesnothConnection.connect();

        boolean result = wesnothConnection.isAlive();

        assertThat(result, is(true));
    }

}