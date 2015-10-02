package org.wesnoth.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wesnoth.network.compression.GzipUnzip;
import org.wesnoth.network.protocol.ArrayTrimmer;
import org.wesnoth.network.protocol.SizeConverter;
import org.wesnoth.network.toolsupport.WiresharkByteConverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class WiresharkConverterCLI {

    private static final Logger LOGGER = LoggerFactory.getLogger(WiresharkConverterCLI.class);

    private WiresharkConverterCLI() {
    }

    public static void main(String ... args) {
        byte[] bytes = WiresharkByteConverter.hexStringToByteArray(args[0]);
        int packetSize = SizeConverter.convertFirstFourBytesToSize(bytes);
        LOGGER.info("Packetsize: " + packetSize);
        try {
            LOGGER.info("Content:");
            String unzip = new GzipUnzip().unzip(new ByteArrayInputStream(ArrayTrimmer.skipFirstFourBytes(bytes)));
            LOGGER.info(unzip);
        } catch (IOException e) {
            LOGGER.error("Problem with unzip.", e);
        }

    }
}
