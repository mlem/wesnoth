package org.wesnoth.network.protocol;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.wesnoth.network.toolsupport.WiresharkByteConverter.hexStringToByteArray;

public class SizeConverterTest {

    @Test
    public void testSizeConverter() {
        String wiresharkValue = "00:00:00:25:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:ca:cf:cc:8b:cf:c9:4f:4a:aa:8c:e5:8a:d6:47:e6:01:00:44:c8:fa:49:1b:00:00:00";

        assertThat(SizeConverter.convertFirstFourBytesToSize(hexStringToByteArray(wiresharkValue)), is(37));
    }


    @Test
    public void testSizeConverter40Megabyte() {
        String wiresharkValue = "02:62:5A:00";

        assertThat(SizeConverter.convertFirstFourBytesToSize(hexStringToByteArray(wiresharkValue)), is(40000000));
    }
}