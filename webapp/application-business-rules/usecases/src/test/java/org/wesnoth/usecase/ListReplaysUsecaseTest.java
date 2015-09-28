package org.wesnoth.usecase;

import org.junit.Test;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGateway;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListReplaysUsecaseTest {

    @Test
    public void test() throws ExternalServiceException {
        ReplayGateway replayGateway = mock(ReplayGateway.class);
        when(replayGateway.listReplays(any(ReplayConnection.class))).thenReturn(Arrays.asList(new ReplayInfo(null, null, null, 0, null, null, null, null, 1, null)));
        ListReplaysUsecase listReplaysUsecase = new ListReplaysUsecase(replayGateway);

        ListReplaysUsecase.Request request = new ListReplaysUsecase.Request(mock(ReplayConnection.class));
        ListReplaysUsecase.Response response = new ListReplaysUsecase.Response();
        listReplaysUsecase.execute(request, response);

        assertThat(response.success(), is(true));
        assertThat(response.foundReplays().size(), is(1));
    }

}