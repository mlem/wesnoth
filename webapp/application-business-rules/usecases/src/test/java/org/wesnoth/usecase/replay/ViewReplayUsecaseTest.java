package org.wesnoth.usecase.replay;

import org.junit.Test;
import org.wesnoth.connection.ExternalServiceException;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.Replay;
import org.wesnoth.gateway.replays.ReplayGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ViewReplayUsecaseTest implements Observer {
    private List<Event> events = new ArrayList<>();

    @Test
    public void test() throws ExternalServiceException {
        ReplayGateway replayGateway = mock(ReplayGateway.class);
        when(replayGateway.loadReplay(anyObject(), anyObject())).thenReturn(new Replay());
        ViewReplayUsecase viewReplayUsecase = new ViewReplayUsecase(replayGateway);
        ViewReplayUsecase.Response response = new ViewReplayUsecase.Response();
        ReplayConnection replayConnection = mock(ReplayConnection.class);
        ViewReplayUsecase.Request request = new ViewReplayUsecase.Request(replayConnection, this);
        viewReplayUsecase.execute(request, response);

        assertThat(events.get(0),is(ReplayLoader.Event.LOADED));
        assertThat(events.get(1),is(Replay.Event.FINISH));
    }

    @Override
    public void update(Observable o, Object arg) {
        events.add((Event) arg);
    }
}