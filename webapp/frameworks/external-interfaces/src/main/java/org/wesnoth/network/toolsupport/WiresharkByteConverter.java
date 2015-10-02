package org.wesnoth.network.toolsupport;

import javax.xml.bind.DatatypeConverter;

public class WiresharkByteConverter {

    private WiresharkByteConverter() {
    }

    public static byte[] hexStringToByteArray(String wiresharkValue) {
        String cleanHexString = wiresharkValue.replace(":", "");
        return DatatypeConverter.parseHexBinary(cleanHexString);
    }
}
