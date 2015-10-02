package org.wesnoth.gateway.replays;

import org.itadaki.bzip2.BZip2InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wesnoth.ReplayMeta;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.replay.ReplayLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class ReplayGatewayImpl implements ReplayGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayGateway.class);

    private static final HtmlToListOfReplaysParser parser = new HtmlToListOfReplaysParser();

    @Override
    public List<ReplayMeta> listReplays(ReplayConnection replayConnection) throws ExternalServiceException {
        List<ReplayMeta> replayMetas;

        try (InputStream inputStream = replayConnection.connect()) {
            replayMetas = parser.parseStreamToListOfReplays(inputStream, replayConnection.currentUrl());
        } catch (IOException e) {
            throw new ExternalServiceException("Problem during read of stream", e);
        } catch (URISyntaxException e) {
            throw new ExternalServiceException("Couldn't create URI from link", e);
        }
        return replayMetas;
    }

    @Override
    public ReplayLoader loadReplay(ReplayLoader replayLoader, ReplayConnection replayConnection) throws ExternalServiceException {

        String line;
        try (InputStream inputStream = replayConnection.connect();
             BZip2InputStream stream = new BZip2InputStream(inputStream, false);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {
            line = bufferedReader.readLine();
            while (line != null) {
                replayLoader.addLine(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            LOGGER.error("problem", e);
        } catch (ExternalServiceException e) {
            LOGGER.error("problem", e);
        }
        return replayLoader;
    }

}
