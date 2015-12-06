package org.wesnoth.replay;

import org.wesnoth.wml.WMLTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WmlConverter {


    public WMLTag convert(List<String> lines) {
        return convertTo("root", lines.iterator());
    }

    public WMLTag convertTo(String tagName, Iterator<String> iterator) {
        WMLTag root = new WMLTag(tagName);
        while (iterator.hasNext()) {
            String line = cutOffComments(iterator.next());

            if (line.contains("=")) {
                String key = line.split("=")[0].trim();
                String value = line.split("=")[1];
                if (value.startsWith("\"") && !value.trim().equals("\"\"")) {
                    while (iterator.hasNext() && (!value.endsWith("\"") || value.endsWith("\"\""))) {
                        value += "\n" + iterator.next().trim();
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

    private String cutOffComments(String lineWithComment) {
        String line;
        if(lineWithComment.contains("#")) {
            line = lineWithComment.substring(0, lineWithComment.indexOf("#"));
        } else {
            line = lineWithComment;
        }
        return line;
    }

    public WMLTag convert(InputStream connection) {
        String line;
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection, StandardCharsets.UTF_8))) {
            line = bufferedReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
            return convert(lines);
        } catch (IOException e) {
            // LOGGER.error("problem", e);
        }
        return new WMLTag("root");

    }
}
