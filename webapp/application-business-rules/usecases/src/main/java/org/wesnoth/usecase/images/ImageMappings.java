package org.wesnoth.usecase.images;

import org.wesnoth.wml.WMLTag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImageMappings {
    private WMLTag wmlTag;

    public ImageMappings(WMLTag wmlTag) {

        this.wmlTag = wmlTag;
    }

    public Optional<ImageMapping> of(TagType tagType, String terrainName) {
        return this.wmlTag.findAllTags(tagType)
                .stream()
                .filter(wmlTag1 -> {
                    String string = wmlTag1.getAttribute("string");
                    return terrainName.equals(string);
                })
                .findFirst()
                .map(ImageMapping::new);
    }

    public List<ImageMapping> asList() {
        return wmlTag.children()
                .stream()
                .map(ImageMapping::new)
                .collect(Collectors.toList());
    }
}
