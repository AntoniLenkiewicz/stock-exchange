package com.antoni.exchange.controllers;

import com.antoni.exchange.models.OrderReceipt;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderReceiptPublisher {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public OrderReceiptPublisher(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendReceipt(OrderReceipt receipt) {
        simpMessagingTemplate.convertAndSend("/topic/receipts", receipt);
    }
}
