package org.wesnoth.servlet.replay;

import java.net.URI;
import java.util.List;

public class ViewReplayDto {
    private final URI downloadUri;
    private final List<ImageDto> images;

    public ViewReplayDto(URI downloadUri, List<ImageDto> images) {

        this.downloadUri = downloadUri;
        this.images = images;
    }

    public URI getDownloadUri() {
        return downloadUri;
    }

    public List<ImageDto> getImages() {
        return images;
    }
}
