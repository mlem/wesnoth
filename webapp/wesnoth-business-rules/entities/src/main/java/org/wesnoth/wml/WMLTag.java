package org.wesnoth.wml;

import java.util.ArrayList;
import java.util.List;

public class WMLTag {

    private String name;
    private WMLTag parent;
    private List<WMLTag> subTags = new ArrayList<>();
    private String attributes;

    public WMLTag(String name) {
        this.name = name;
    }

    public void addTag(WMLTag current) {
        subTags.add(current);
        current.parent = this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
