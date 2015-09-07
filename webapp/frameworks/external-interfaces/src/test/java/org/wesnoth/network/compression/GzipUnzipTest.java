package org.wesnoth.network.compression;

import org.junit.Test;
import org.wesnoth.network.protocol.SizeConverter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.wesnoth.network.toolsupport.WiresharkByteConverter.*;

public class GzipUnzipTest {

    private GzipUnzip gzipUnzip = new GzipUnzip();

    @Test
    public void testUnzip() throws IOException {
        byte[] zipped = {31, -117, 8, 0, 0, 0, 0, 0, 0, -1, 93, -114, 65, 14, -61, 32, 12, 4, -17, 126, -122, 63, 64, 63, -64, -91, -3, 66, 15, -107, 16, 66, 38, 56, -87, 37, 23, 36, 32, 85, -13, -5, -48, -26, -44, 92, 71, -69, 59, -21, 22, 122, -79, 74, -21, 33, -55, 60, 123, 112, -110, 27, -41, 30, -90, -89, 104, -14, 32, 57, -15, -57, -30, 5, -63, -83, -125, 123, -96, 55, -119, 82, 84, -74, -72, 113, 67, -8, -42, -125, -92, 95, 68, -53, 68, 93, 74, -74, -120, -112, 7, -73, 120, -27, -14, -72, -33, 16, 42, 47, 67, -63, -107, 71, 48, 23, -124, -42, -87, -81, -51, -94, -106, 24, -73, -79, 109, -114, 113, 103, -2, -19, -50, -100, -34, -19, 81, -23, -124, -49, -82, 0,
                0,0
        };

        String expected = "[gamelist_diff]\n" +
                "[insert_child]\n" +
                "index=\"0\"\n" +
                "[user]\n" +
                "available=\"yes\"\n" +
                "game_id=\"0\"\n" +
                "location=\"\"\n" +
                "name=\"BeoXTC\"\n" +
                "registered=\"no\"\n" +
                "status=\"lobby\"\n" +
                "[/user]\n" +
                "[/insert_child]\n" +
                "[/gamelist_diff]\n";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipped);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));

        int result = bufferedReader.read();
        StringBuilder sb = new StringBuilder();
        while (result != -1) {
            sb.append((char) result);
            result = bufferedReader.read();
        }

        assertThat(sb.toString(), is(expected));
    }


    @Test
    public void testHexStream() throws IOException {
        byte[] zipped = {(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x45, (byte) 0x8d, (byte) 0x41, (byte) 0x0a, (byte) 0xc3, (byte) 0x30, (byte) 0x0c, (byte) 0x04, (byte) 0xef, (byte) 0x7a, (byte) 0xc6, (byte) 0x7e, (byte) 0xa0, (byte) 0xfd, (byte) 0x80, (byte) 0x2e, (byte) 0xed, (byte) 0x17, (byte) 0x7a, (byte) 0x28, (byte) 0x98, (byte) 0x50, (byte) 0xe4, (byte) 0x44, (byte) 0x04, (byte) 0x83, (byte) 0x6a, (byte) 0x81, (byte) 0xe5, (byte) 0x14, (byte) 0xf2, (byte) 0xfb, (byte) 0x38, (byte) 0xe4, (byte) 0xd0, (byte) 0xe3, (byte) 0x0e, (byte) 0xc3, (byte) 0x6c, (byte) 0x5a, (byte) 0xe5, (byte) 0xab, (byte) 0x56, (byte) 0xa2, (byte) 0x4f, (byte) 0x44, (byte) 0xe9, (byte) 0xf6, (byte) 0x1f, (byte) 0x69, (byte) 0x0b, (byte) 0x6d, (byte) 0x13, (byte) 0xc9, (byte) 0x4f, (byte) 0x8a, (byte) 0x49, (byte) 0x36, (byte) 0x65, (byte) 0xec, (byte) 0x1a, (byte) 0xa0, (byte) 0x53, (byte) 0xf8, (byte) 0x94, (byte) 0x85, (byte) 0x71, (byte) 0x07, (byte) 0x99, (byte) 0xcf, (byte) 0xd2, (byte) 0x8b, (byte) 0x57, (byte) 0x06, (byte) 0xa8, (byte) 0x0e, (byte) 0xce, (byte) 0x78, (byte) 0xa8, (byte) 0xbf, (byte) 0x5f, (byte) 0x4f, (byte) 0x50, (byte) 0xd3, (byte) 0x75, (byte) 0x44, (byte) 0xb4, (byte) 0xe9, (byte) 0x10, (byte) 0xab, (byte) 0x83, (byte) 0xa2, (byte) 0x4b, (byte) 0xdf, (byte) 0x82, (byte) 0x61, (byte) 0x9e, (byte) 0xf3, (byte) 0x8e, (byte) 0xf1, (byte) 0x73, (byte) 0xc5, (byte) 0x0f, (byte) 0x77, (byte) 0x48, (byte) 0x23, (byte) 0x29, (byte) 0x7c, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        String expected = "[gamelist]\n\n[/gamelist]\n[user]\navailable=\"yes\"\ngame_id=\"0\"\nlocation=\"\"\nname=\"BeoXTC\"\nregistered=\"no\"\nstatus=\"lobby\"\n[/user]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(zipped));
        assertThat(actualString, is(expected));
    }


    @Test
    public void testWiresharkStream() throws IOException {
        String wiresharkValue = "00:00:00:79:1f:8b:08:00:00:00:00:00:00:ff:45:8d:41:0a:c3:30:0c:04:ef:7a:c6:7e:a0:fd:80:2e:ed:17:7a:28:98:50:e4:44:04:83:6a:81:e5:14:f2:fb:38:e4:d0:e3:0e:c3:6c:5a:e5:ab:56:a2:4f:44:e9:f6:1f:69:0b:6d:13:c9:4f:8a:49:36:65:ec:1a:a0:53:f8:94:85:71:07:99:cf:d2:8b:57:06:a8:0e:ce:78:a8:bf:5f:4f:50:d3:75:44:b4:e9:10:ab:83:a2:4b:df:82:61:9e:f3:8e:f1:73:c5:0f:77:48:23:29:7c:00:00:00";

        String expected = "[gamelist]\n\n[/gamelist]\n[user]\navailable=\"yes\"\ngame_id=\"0\"\nlocation=\"\"\nname=\"BeoXTC\"\nregistered=\"no\"\nstatus=\"lobby\"\n[/user]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }


    @Test
    public void testFirstCallFromServer() throws IOException {
        String wiresharkValue = "00:00:00:21:1f:8b:08:00:00:00:00:00:00:ff:8b:2e:4b:2d:2a:ce:cc:cf:8b:e5:8a:d6:87:33:01:20:bc:17:06:15:00:00:00";

        String expected = "[version]\n[/version]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }

    @Test
    public void testFirstAnswerFromClient() throws IOException {
        String wiresharkValue = "00:00:00:33:1f:8b:08:00:00:00:00:00:00:ff:8b:2e:4b:2d:2a:ce:cc:cf:8b:e5:e2:84:b2:6c:95:0c:f5:0c:8d:f5:0c:b5:53:52:cb:94:b8:a2:f5:e1:0a:b8:00:93:52:36:21:2c:00:00:00";

        String expected = "[version]\n\tversion=\"1.13.1+dev\"\n[/version]\n\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }


    @Test
    public void testSecondCallFromServer() throws IOException {
        String wiresharkValue = "00:00:00:24:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:2d:2d:2e:c9:c9:4f:cf:cc:8b:e5:8a:d6:47:e2:00:00:62:de:28:66:19:00:00:00";

        String expected = "[mustlogin]\n[/mustlogin]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }

    @Test
    public void testSecondAnswerFromClient() throws IOException {
        String wiresharkValue = "00:00:00:48:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:c9:4f:cf:cc:8b:e5:e2:2c:4e:cd:49:4d:2e:c9:2c:4b:8d:2f:c8:cc:4b:b7:ad:4c:2d:e6:e2:2c:2d:4e:2d:ca:4b:cc:4d:b5:55:72:4a:cd:8f:08:71:56:e2:8a:d6:87:aa:e7:02:00:e6:3b:eb:fe:39:00:00:00";

        String expected = "[login]\n\tselective_ping=yes\n\tusername=\"BeoXTC\"\n[/login]\n\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }


    @Test
    public void testThirdCallFromServer() throws IOException {
        String wiresharkValue = "00:00:00:25:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:ca:cf:cc:8b:cf:c9:4f:4a:aa:8c:e5:8a:d6:47:e6:01:00:44:c8:fa:49:1b:00:00:00";

        String expected = "[join_lobby]\n[/join_lobby]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }


    @Test
    public void testForthCallFromServer() throws IOException {
        String wiresharkValue = "00:00:00:79:1f:8b:08:00:00:00:00:00:00:ff:45:8d:41:0a:c3:30:0c:04:ef:7a:c6:7e:a0:fd:80:2e:ed:17:7a:28:98:50:e4:44:04:83:6a:81:e5:14:f2:fb:38:e4:d0:e3:0e:c3:6c:5a:e5:ab:56:a2:4f:44:e9:f6:1f:69:0b:6d:13:c9:4f:8a:49:36:65:ec:1a:a0:53:f8:94:85:71:07:99:cf:d2:8b:57:06:a8:0e:ce:78:a8:bf:5f:4f:50:d3:75:44:b4:e9:10:ab:83:a2:4b:df:82:61:9e:f3:8e:f1:73:c5:0f:77:48:23:29:7c:00:00:00";

        String expected = "[gamelist]\n\n[/gamelist]\n[user]\navailable=\"yes\"\ngame_id=\"0\"\nlocation=\"\"\nname=\"BeoXTC\"\nregistered=\"no\"\nstatus=\"lobby\"\n[/user]\n";

        String actualString = gzipUnzip.unzip(skipFirstFourBytes(hexStringToByteArray(wiresharkValue)));
        assertThat(actualString, is(expected));
    }

    /**
     * first four bytes represent something in wesnoth which I don't know for sure what. My guess is it is the client ID or an Identifier to
     *
     * @param zipped
     * @return
     */
    private byte[] skipFirstFourBytes(byte[] zipped) {
        return Arrays.copyOfRange(zipped, 4, zipped.length);
    }
}
