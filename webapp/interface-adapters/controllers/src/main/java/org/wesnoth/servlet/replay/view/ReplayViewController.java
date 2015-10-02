package org.wesnoth.servlet.replay.view;

import org.springframework.stereotype.Service;
import org.wesnoth.connection.replays.ReplayConnection;
import org.wesnoth.gateway.replays.ReplayGatewayImpl;
import org.wesnoth.usecase.replay.ViewReplayUsecase;

import java.util.Observer;

@Service
public class ReplayViewController {
    public void viewReplay(ReplayConnection replayConnection, Observer observer) {
        ViewReplayUsecase.Response response = new ViewReplayUsecase.Response();
        new ViewReplayUsecase(new ReplayGatewayImpl())
                .execute(new ViewReplayUsecase.Request(replayConnection, observer), response);

    }
}
