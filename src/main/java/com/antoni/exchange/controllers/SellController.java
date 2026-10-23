package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.queue.queueServices.OrderProcessor;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Controller
public class SellController {
    private final OrderProcessor orderProcessor;

    public SellController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @MessageMapping("/sell")
    @SendTo("/topic/sell")
    public Order placeSellOrder(@RequestBody @Valid Order order){
        orderProcessor.processOrder(order);
        return order;
        //Start a websocket connection, and publish the update after writing the order to confirm its accepted
        //How to return that the order has been accepted? as the queueprocessor decides that?
    }
}
