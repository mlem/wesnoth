package org.wesnoth.network;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.gzip.GzipUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

public class SocketClient2Test {


    public static void main(String... args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress addr = new InetSocketAddress("localhost", 15000);
        socketChannel.connect(addr);

        System.out.println("connecting to " + addr.getHostString() + ":" + addr.getPort());
        Thread thread = new Thread(() -> {
            ByteBuffer buf = ByteBuffer.allocate(500);
            while (true) {
                int bytesRead = 0;
                try {
                    bytesRead = socketChannel.read(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ThreadDeath();

                }

                if (bytesRead > 0) {
                    byte[] array = prepend(prepend(prepend(buf.array(), (byte) 8), (byte) 139), (byte) 31) ;
                    ByteArrayInputStream bufferInputStream = new ByteArrayInputStream(array);
                    try (InputStream stream = getStreamForCompressedFile(bufferInputStream)){

                        ByteBuffer buffer = ByteBuffer.allocate(48);

                        stream.read(buffer.array());


                        Charset utf16 = Charset.forName("UTF-16");
                        CharBuffer chbf = utf16.decode(buf);
                        System.out.println("read from server: \"" + chbf + "\"");
                       /* String readLine = bufferedReaderForCompressedFile.readLine();
                        while(readLine != null) {
                            System.out.println(readLine);
                            readLine = bufferedReaderForCompressedFile.readLine();
                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (CompressorException e) {
                        e.printStackTrace();
                    }
                    Charset utf16 = Charset.forName("UTF-16");
                    CharBuffer chbf = utf16.decode(buf);
                    System.out.println("read from server: \"" + chbf + "\"");
                }
            }

        });
        thread.start();
        Scanner scanner = new Scanner(System.in);
        String sentence;
        try {
            do {
                System.out.print("> ");
                sentence = scanner.next();
                writeToSocketChannel(socketChannel);
            } while (!"quit".equals(sentence));
        } finally {
            socketChannel.close();
        }

    }

    private static byte[] prepend(byte[] array, byte element) {
        byte [ ]  newArray = ( byte [ ] ) Array.newInstance(
                array.getClass().getComponentType(), array.length + 1);

        System.arraycopy(array, 0, newArray, 1, array.length);

        newArray [ 0 ] = element;

        return newArray;
    }

    private static void writeToSocketChannel(SocketChannel socketChannel) throws IOException {
        String newData = "[version]\n[/version]\n";

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }
    }

    public static InputStream getStreamForCompressedFile(InputStream fileIn) throws IOException, CompressorException {
        return new DeflaterInputStream(fileIn);
    }

    public static BufferedReader getBufferedReaderForCompressedFile(InputStream fileIn) throws IOException, CompressorException {
        //BZip2InputStream inputStream = new BZip2InputStream(fileIn, false);
        DeflaterInputStream inputStream = new DeflaterInputStream(fileIn);
       // java.util.zip.GZIPInputStream inputStream = new java.util.zip.GZIPInputStream(fileIn);
        //CompressorInputStream inputStream = new CompressorStreamFactory().createCompressorInputStream(fileIn);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(inputStream));
        return br2;
    }
}
