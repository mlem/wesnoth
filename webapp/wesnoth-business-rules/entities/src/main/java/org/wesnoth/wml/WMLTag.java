package org.wesnoth.wml;

import java.util.*;
import java.util.stream.Collectors;

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

    public Optional<WMLTag> findTag(String tagName) {
        return tagList
                .stream()
                .filter(wmlTag -> wmlTag.tagName.equals(tagName))
                .findFirst();
    }

    public List<WMLTag> findAllTags(org.wesnoth.usecase.images.TagType tagType) {
        List<WMLTag> result = tagList
                .stream()
                .filter(wmlTag -> wmlTag.tagName.equals(tagType.name().toLowerCase()))
                .collect(Collectors.toList());
        return result;
    }

    public List<WMLTag> children() {
        return tagList
                .stream()
                .collect(Collectors.toList());
    }
}
