package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.models.Ticker;
import com.antoni.exchange.orderStorage.TickerMap;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import com.antoni.exchange.queue.queueServices.OrderProcessor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
public class BuyController {

    private final OrderProcessor orderProcessor;

    public BuyController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping("/buy")
    public Order placeBuyOrder(@RequestBody @Valid Order order) {
        orderProcessor.processOrder(order);
        return order;
    }
}
