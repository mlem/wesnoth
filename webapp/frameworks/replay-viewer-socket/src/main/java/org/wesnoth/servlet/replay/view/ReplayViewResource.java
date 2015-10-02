package org.wesnoth.servlet.replay.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.wesnoth.network.connection.ReplayHttpConnection;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Observable;
import java.util.Observer;

@Controller
public class ReplayViewResource implements Observer{


    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ReplayViewController controller;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String replayUri) throws Exception {

            try {
                String decodedUrl = URLDecoder.decode(replayUri.replaceAll("\\+", "%2b"), "UTF-8");

                ReplayHttpConnection replayHttpConnection = new ReplayHttpConnection(decodedUrl);
                controller.viewReplay(replayHttpConnection, this);

            } catch (IOException e) {
                // TODO: don't print stacktrace. LOG or do something else. Maybe return something to the WebSocket
                e.printStackTrace();
            }
        return "Loading, Replay from URL " + replayUri + "!";
    }

    public void fireGreeting(String line) {
        System.out.println(line);
        this.template.convertAndSend("/topic/greetings", line);
    }

    @Override
    public void update(Observable o, Object arg) {
        fireGreeting((String) arg);
    }
}