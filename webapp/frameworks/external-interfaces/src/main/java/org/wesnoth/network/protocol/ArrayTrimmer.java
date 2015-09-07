package org.wesnoth.network.protocol;

import java.util.Arrays;

public class ArrayTrimmer {


    /**
     * first four bytes represent something in wesnoth which I don't know for sure what. My guess is it is the client ID or an Identifier to
     *
     * @param zipped
     * @return
     */
    public static byte[] skipFirstFourBytes(byte[] zipped) {
        return Arrays.copyOfRange(zipped, 4, zipped.length);
    }
}
