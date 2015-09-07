package org.wesnoth.tools;

import org.wesnoth.network.compression.GzipUnzip;
import org.wesnoth.network.protocol.ArrayTrimmer;
import org.wesnoth.network.protocol.SizeConverter;
import org.wesnoth.network.toolsupport.WiresharkByteConverter;

import java.io.IOException;

public class WiresharkConverterCLI {

    public static void main(String ... args) {
        byte[] bytes = WiresharkByteConverter.hexStringToByteArray(args[0]);
        int packetSize = SizeConverter.convertFirstFourBytesToSize(bytes);
        System.out.println("Packetsize: " + packetSize);
        try {
            System.out.println("Content:");
            String unzip = new GzipUnzip().unzip(ArrayTrimmer.skipFirstFourBytes(bytes));
            System.out.println(unzip);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
