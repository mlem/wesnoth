package org.wesnoth.servlet.replay.view;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Observer;

@Service
public class ReplayViewController {
    public void consumeStream(InputStream stream, Observer observer) {
        ViewReplayUsecase.Response response = new ViewReplayUsecase.Response();
        new ViewReplayUsecase(new ViewReplayUsecase.Request(stream, observer), response)
        .execute();

    }
}
