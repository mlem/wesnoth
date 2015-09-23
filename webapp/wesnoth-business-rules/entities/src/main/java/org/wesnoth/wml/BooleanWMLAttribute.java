package org.wesnoth.wml;

import org.wesnoth.wml.WMLAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BooleanWMLAttribute implements WMLAttribute {
    private boolean value;

    public BooleanWMLAttribute(boolean value) {
        this.value = value;
    }

    @Override
    public void writeExternal(OutputStream out) throws IOException {
        String stringValue = value ? "yes" : "no";
        out.write(stringValue.getBytes());
    }

    @Override
    public void readExternal(InputStream in) throws IOException, ClassNotFoundException {

    }
}
