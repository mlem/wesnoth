package org.wesnoth.servlet.replay;

import org.plutext.jaxb.svg11.Svg;
import org.wesnoth.usecase.images.ImageMappingUsecase;

import java.util.List;

public interface ImageController {
    List<ImageDto> listImages(ImageMappingUsecase.Request request);

    Svg definitionSvg();
}
