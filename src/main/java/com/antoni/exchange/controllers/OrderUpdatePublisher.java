package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class OrderUpdatePublisher {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public OrderUpdatePublisher (SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendOrderUpdate(Order order) {
        simpMessagingTemplate.convertAndSend("/topic/order", order);
    }
}
