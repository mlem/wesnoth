package org.wesnoth.servlet.replay.view;

import org.itadaki.bzip2.BZip2InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

@Controller
public class ViewController {


    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String replayUri) throws Exception {

        new Thread(() -> {
            try {
                String decodedUrl = URLDecoder.decode(replayUri.replaceAll("\\+", "%2b"), "UTF-8");
                URL uri = new URL(decodedUrl);
                InputStream inputStream = uri.openStream();
                BZip2InputStream stream = new BZip2InputStream(inputStream, false);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                String line = bufferedReader.readLine();
                while(line != null) {
                    fireGreeting(line);
                    line= bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {
                // TODO: don't print stacktrace. LOG or do something else. Maybe return something to the WebSocket
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return "Loading, Replay from URL " + replayUri + "!";
    }


    public void fireGreeting(String line) {
        System.out.println(line);
        this.template.convertAndSend("/topic/greetings", line);
    }

}