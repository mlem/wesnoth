package org.wesnoth.network.serializer;

import java.io.*;

public interface WesnothSerializable {

    public void writeExternal(OutputStream out) throws IOException;

    public void readExternal(InputStream in) throws IOException, ClassNotFoundException;
}
