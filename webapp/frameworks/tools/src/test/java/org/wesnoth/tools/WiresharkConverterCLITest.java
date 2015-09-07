package org.wesnoth.tools;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class WiresharkConverterCLITest {

    @Test
    public void testWiresharkConverter() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String wiresharkValue = "00:00:00:25:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:ca:cf:cc:8b:cf:c9:4f:4a:aa:8c:e5:8a:d6:47:e6:01:00:44:c8:fa:49:1b:00:00:00";

        WiresharkConverterCLI.main(wiresharkValue);
        String newLine = System.lineSeparator();
        String expected =
                "Packetsize: 37"+newLine+
                "Content:"+newLine+
                "[join_lobby]\n[/join_lobby]\n"+newLine;
        assertThat(out.toString(), is(expected));
    }
}