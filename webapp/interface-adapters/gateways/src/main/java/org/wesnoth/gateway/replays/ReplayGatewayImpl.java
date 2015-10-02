package org.wesnoth.gateway.replays;

import org.itadaki.bzip2.BZip2InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.usecase.ReplayInfo;
import org.wesnoth.usecase.replay.ViewReplayUsecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

public class ReplayGatewayImpl implements ReplayGateway {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReplayGateway.class);

    private final static HtmlToListOfReplaysParser parser = new HtmlToListOfReplaysParser();

    @Override
    public List<ReplayInfo> listReplays(ReplayConnection replayConnection) throws ExternalServiceException {
        List<ReplayInfo> replayInfos;

        try (InputStream inputStream = replayConnection.connect()) {
            replayInfos = parser.parseStreamToListOfReplays(inputStream, replayConnection.currentUrl());
        } catch (IOException e) {
            throw new ExternalServiceException("Problem during read of stream", e);
        } catch (URISyntaxException e) {
            throw new ExternalServiceException("Couldn't create URI from link", e);
        }
        return replayInfos;
    }

    @Override
    public void loadReplay(ViewReplayUsecase.ReplayViewer replayViewer, ReplayConnection replayConnection) throws ExternalServiceException {

        String line = null;
        try (InputStream inputStream = replayConnection.connect();
             BZip2InputStream stream = new BZip2InputStream(inputStream, false);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream))) {
            line = bufferedReader.readLine();
            while (line != null) {
                replayViewer.addLine(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            LOGGER.error("problem", e);
        } catch (ExternalServiceException e) {
            LOGGER.error("problem", e);
        }

    }

}
