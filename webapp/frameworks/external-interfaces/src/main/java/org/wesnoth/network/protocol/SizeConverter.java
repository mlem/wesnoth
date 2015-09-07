package org.wesnoth.network.protocol;

public class SizeConverter {
    public static int convertFirstFourBytesToSize(byte[] bytes) {
        return bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3];
    }
}
