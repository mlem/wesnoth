package org.wesnoth.usecase.images;

import org.wesnoth.replay.WmlConverter;
import org.wesnoth.wml.WMLTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigGateway {

    private List<String> lines = new ArrayList<>();
    private InputStream connection;

    public ConfigGateway(InputStream connection) {

        this.connection = connection;
    }

    public WMLTag toWmlTag() {
        String line;
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection, StandardCharsets.UTF_8))) {
            line = bufferedReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
            return new WmlConverter().convert(lines);
        } catch (IOException e) {
           // LOGGER.error("problem", e);
        }
        return new WMLTag("root");
    }
}
