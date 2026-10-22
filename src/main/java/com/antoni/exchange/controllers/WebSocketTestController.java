package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Greeting;
import com.antoni.exchange.models.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketTestController {
    //pass controller as bean to wal to publish order acceptance/status after logging
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello!");

    }
}
