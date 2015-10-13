package org.wesnoth.replay;

import org.wesnoth.wml.WMLTag;

import java.util.Iterator;
import java.util.List;

public class WmlConverter {


    public WMLTag convert(List<String> lines) {
        return convertTo("root", lines.iterator());
    }

    public WMLTag convertTo(String tagName, Iterator<String> iterator) {
        WMLTag root = new WMLTag(tagName);
        String line;
        while (iterator.hasNext()) {
            line = iterator.next();
            if (line.contains("=")) {
                String key = line.split("=")[0].trim();
                String value = line.split("=")[1];
                if(value.startsWith("\"") && !value.trim().equals("\"\"")) {
                    while (iterator.hasNext() && (!value.endsWith("\"") || value.endsWith("\"\""))) {
                        value+= "\n" + iterator.next().trim();
                    }
                }
                root.addAttribute(key, value.replace("\"", ""));
            } else if (line.contains("[/") && line.contains("]")) {
                return root;
            } else if (line.contains("[") && line.contains("]")) {
                root.addTag(convertTo(line.replaceAll("\\[", "").replaceAll("\\]", ""), iterator));
            }
        }
        return root;
    }

}
