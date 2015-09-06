package org.wesnoth.network.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WMLTag implements WesnothSerializable{
    private String tagName;
    private Map<String, WMLAttribute> attributes = new HashMap<>();

    public WMLTag(Class<?> tagName) {
        this.tagName = tagName.getSimpleName().toLowerCase();
    }

    public void putAttribute(String name, WMLAttribute value) {
        attributes.put(name, value);
    }

    @Override
    public void writeExternal(OutputStream out) throws IOException {
        String openTag = "[" + tagName + "]\n";
        String closeTag = "[/" + tagName + "]\n";

        out.write(openTag.getBytes());

        writeAttributes(out);

        out.write(closeTag.getBytes());
    }

    private void writeAttributes(OutputStream out) throws IOException {
        for(Map.Entry<String, WMLAttribute> keyValue : attributes.entrySet()) {
            out.write(("\t"+keyValue.getKey() + "=").getBytes());
            keyValue.getValue().writeExternal(out);
            out.write('\n');
        }
    }

    @Override
    public void readExternal(InputStream in) throws IOException, ClassNotFoundException {

    }
}
