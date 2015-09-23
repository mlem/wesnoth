package org.wesnoth.wml;

import java.io.*;

public interface WesnothSerializable {

    public void writeExternal(OutputStream out) throws IOException;

    public void readExternal(InputStream in) throws IOException, ClassNotFoundException;
}
