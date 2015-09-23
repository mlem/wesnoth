package org.wesnoth.usecase;

import org.junit.Test;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListReplaysUsecaseTest {

    @Test
    public void test() throws ExternalServiceException {
        ReplayGateway replayGateway = mock(ReplayGateway.class);
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(replayGateway);

        ReplayConnection replayConnection = mock(ReplayConnection.class);
        when(replayConnection.connectAndExecute()).thenReturn(getClass().getResourceAsStream("replays_1.12_20150921.html"));
        when(replayConnection.currentUrl()).thenReturn("replays.wesnoth.org/1.12/20150921/");

        ListReplaysUsecase.Request request = new ListReplaysUsecase.Request(replayConnection);
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);

        assertThat(response.success(), is(true));
        assertThat(response.foundReplays().size(), is(1));
    }

}