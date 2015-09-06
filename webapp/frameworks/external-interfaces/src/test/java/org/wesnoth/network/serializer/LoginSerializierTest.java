package org.wesnoth.network.serializer;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LoginSerializierTest {

    @Test
    public void testSerialization() throws IOException {
        Login login = new Login();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        login.writeExternal(byteArrayOutputStream);

        String result = new String(byteArrayOutputStream.toByteArray()) + "\n";
        String expected = "[login]\n\tselective_ping=yes\n\tusername=\"BeoXTC\"\n[/login]\n\n";

        assertThat(result, is(expected));
    }

}
