package org.wesnoth.network;

import javax.xml.bind.DatatypeConverter;

public class WiresharkByteConverter {
    public static byte[] hexStringToByteArray(String wiresharkValue) {
        String cleanHexString = wiresharkValue.replace(":", "");
        return DatatypeConverter.parseHexBinary(cleanHexString);
    }
}
