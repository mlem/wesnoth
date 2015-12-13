package org.wesnoth.servlet.replay;

import org.plutext.jaxb.svg11.Svg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wesnoth.usecase.images.ImageMappingUsecase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageControllerImpl implements ImageController {

    private ImageMappingUsecase imageMappingUsecase;

    @Autowired
    public void setImageMappingUsecase(ImageMappingUsecase imageMappingUsecase) {
        this.imageMappingUsecase = imageMappingUsecase;
    }

    @Override
    public List<ImageDto> listImages(ImageMappingUsecase.Request request) {
        ImageMappingUsecase.Response response = new ImageMappingUsecase.Response();
        imageMappingUsecase.execute(request, response);
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

    @Override
    public Svg definitionSvg() {
        List<ImageDto> imageDtos = this.listImages(
                new ImageMappingUsecase.Request(getClass().getResourceAsStream("/data/core/terrain.cfg"))
        );

        return new ImageDtoToSvgConverter().convertToSvg(imageDtos);
    }

}
