package org.wesnoth.wml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WMLTag {
    private String tagName;
    private List<WMLTag> tagList = new LinkedList<>();
    private Map<String, String> attributes = new HashMap<>();

    public WMLTag(String tagName) {

        this.tagName = tagName;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void addTag(WMLTag tag) {
        tagList.add(tag);

    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
