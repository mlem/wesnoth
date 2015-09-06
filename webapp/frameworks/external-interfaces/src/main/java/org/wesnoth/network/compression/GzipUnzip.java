package org.wesnoth.network.compression;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GzipUnzip {

    public String unzip(byte[] zipped) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipped);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));

        int result = bufferedReader.read();
        StringBuilder sb = new StringBuilder();
        while (result != -1) {
            sb.append((char) result);
            result = bufferedReader.read();
        }

        return sb.toString();
    }
}
