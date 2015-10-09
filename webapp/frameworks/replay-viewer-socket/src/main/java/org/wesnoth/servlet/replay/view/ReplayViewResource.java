package org.wesnoth.servlet.replay.view;

import com.sun.xml.internal.txw2.annotation.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.wesnoth.Event;
import org.wesnoth.Tile;
import org.wesnoth.network.connection.ReplayHttpConnection;
import org.wesnoth.replay.Map;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

@Controller
public class ReplayViewResource implements Observer {


    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayViewResource.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ReplayViewController controller;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public void greeting(String replayUri) {

        try {
            String decodedUrl = URLDecoder.decode(replayUri.replaceAll("\\+", "%2b"), "UTF-8");

            ReplayHttpConnection replayHttpConnection = new ReplayHttpConnection(decodedUrl);
            controller.viewReplay(replayHttpConnection, this);

        } catch (IOException e) {
            LOGGER.error("problem viewing the replay.", e);
        }
    }

    public void fireGreeting(Event line) {
        System.out.println(line);
        if(line instanceof Map) {
            new MapDto(convert((Map) line));
        }
        MessageConverter messageConverter = new MessageConverter() {
            @Override
            public Object fromMessage(Message<?> message, Class<?> targetClass) {
                return null;
            }

            @Override
            public Message<?> toMessage(Object payload, MessageHeaders header) {
                return new GenericMessage<>(payload);
            }
        };
        Message<?> message = messageConverter.toMessage(line, new MessageHeaders(null));
        this.template.convertAndSend("/topic/greetings", message);
    }

    private List<List<String>> convert(Map line) {
        return line.getMap().stream()
                .map(row -> row.stream()
                        .map(Tile::getTileString)
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void update(Observable o, Object arg) {
        fireGreeting((Event) arg);
    }

    @XmlElement
    private class MapDto {

        @XmlAttribute
        private List<List<String>> map;

        public MapDto(List<List<String>> map) {
            this.map = map;
        }

        public List<List<String>> getMap() {
            return map;
        }

        public void setMap(List<List<String>> map) {
            this.map = map;
        }
    }
}