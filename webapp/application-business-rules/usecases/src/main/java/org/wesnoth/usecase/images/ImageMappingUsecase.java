package org.wesnoth.usecase.images;

import org.wesnoth.wml.WMLTag;

import java.io.InputStream;

public class ImageMappingUsecase {


    public void execute(Request request, Response response) {
        ConfigGateway configGateway = new ConfigGateway(request.connection());
        WMLTag wmlTag = configGateway.toWmlTag();
        response.imageMappings=new ImageMappings(wmlTag);
    }

    public static class Request {
        private InputStream inputStream;

        public Request(InputStream inputStream) {

            this.inputStream = inputStream;
        }

        public InputStream connection() {
            return inputStream;
        }
    }

    public static class Response {
        private ImageMappings imageMappings;

        public ImageMappings imageMapping() {
            return imageMappings;
        }
    }
}
