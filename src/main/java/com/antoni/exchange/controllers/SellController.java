package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.queue.queueServices.OrderProcessor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class SellController {
    private final OrderProcessor orderProcessor;

    public SellController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/sell")
    public Order placeSellOrder(@RequestBody @Valid Order order){
        orderProcessor.processOrder(order);
        return order;
    }
}
