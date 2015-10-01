package org.wesnoth.servlet.replay.view;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Observer;

@Service
public class ReplayViewController {
    public void consumeStream(InputStream stream, Observer observer) {
        ReplayViewUsecase.Response response = new ReplayViewUsecase.Response();
        new ReplayViewUsecase(new ReplayViewUsecase.Request(stream, observer), response)
        .execute();

    }
}
