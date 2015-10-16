package org.wesnoth.usecase.images;


import org.wesnoth.wml.WMLTag;

import java.net.URI;
import java.net.URISyntaxException;

public class ImageMapping {

    private WMLTag wmlTag;

    public ImageMapping(WMLTag wmlTag) {

        this.wmlTag = wmlTag;
    }

    public URI uri() throws URISyntaxException {
        return new URI("/data/core/images/terrain/" + wmlTag.getAttribute("symbol_image") + ".png");
    }

    public String name() {
        return wmlTag.getAttribute("string").replaceAll("\\^", "").replaceAll("\\/", "-ne-sw");
    }
}
