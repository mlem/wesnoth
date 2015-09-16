package org.wesnoth.network.compression;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GzipUnzip {

    public String unzip(InputStream inputStream) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));

        int result = bufferedReader.read();
        StringBuilder sb = new StringBuilder();
        while (result != -1) {
            sb.append((char) result);
            result = bufferedReader.read();
        }

        return sb.toString();
    }


    public GZIPInputStream unzipToStream(InputStream inputStream) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        return gzipInputStream;
    }
}
