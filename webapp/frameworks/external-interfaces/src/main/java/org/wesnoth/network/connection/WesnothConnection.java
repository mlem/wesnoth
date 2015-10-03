package org.wesnoth.network.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wesnoth.UserName;
import org.wesnoth.network.protocol.SizeConverter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WesnothConnection {


    private final InetAddress wesnothAddress;
    private int port;
    private final UserName userName;
    private static final Logger LOGGER = LoggerFactory.getLogger(WesnothConnection.class);

    public WesnothConnection(InetAddress wesnothAddress, int port, UserName userName) {
        this.wesnothAddress = wesnothAddress;
        this.port = port;
        this.userName = userName;
    }


    public void connect() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress addr = new InetSocketAddress(wesnothAddress.getHostAddress(), port);
        socketChannel.connect(addr);

        ByteBuffer writeBuffer = ByteBuffer.allocate(4);
        writeBuffer.clear();
        writeBuffer.put(new byte[]{0, 0, 0, 0});
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }


        ByteBuffer readBuffer = ByteBuffer.allocate(4);
        int bytesRead = socketChannel.read(readBuffer);
        LOGGER.debug("user " + userName + " is connecting.");
        LOGGER.debug("we read " + bytesRead + " from server");
        LOGGER.debug("response from server: " + SizeConverter.convertFirstFourBytesToSize(readBuffer.array()));

        
    }

    public boolean isAlive() {
        return false;
    }
}
