package org.wesnoth.network.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StringWMLAttribute implements WMLAttribute {
    private String value;

    public StringWMLAttribute(String value) {
        this.value = value;
    }

    @Override
    public void writeExternal(OutputStream out) throws IOException {
        String outputValue = "\"" + value + "\"";
        out.write(outputValue.getBytes());
    }

    @Override
    public void readExternal(InputStream in) throws IOException, ClassNotFoundException {

    }
}
