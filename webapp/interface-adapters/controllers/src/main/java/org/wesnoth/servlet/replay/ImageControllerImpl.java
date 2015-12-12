package org.wesnoth.servlet.replay;

import org.springframework.stereotype.Service;
import org.wesnoth.usecase.images.ImageMappingUsecase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageControllerImpl implements ImageController {
    @Override
    public List<ImageDto> listImages(ImageMappingUsecase.Request request) {
        ImageMappingUsecase.Response response = new ImageMappingUsecase.Response();
        new ImageMappingUsecase().execute(request, response);
        return response.imageMapping()
                .asList()
                .stream().map(mapping -> {
                            try {
                                URI uri = mapping.uri();
                                String name = mapping.id();
                                return new ImageDto(name, uri);
                            } catch (URISyntaxException e) {
                                        // todo:...
                            }
                            return null;
                        }
                ).collect(Collectors.toList());
    }
}
