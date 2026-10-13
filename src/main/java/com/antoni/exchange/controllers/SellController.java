package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.services.OrderProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class SellController {
    private final OrderProcessor orderProcessor;

    public SellController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @GetMapping("/sell")
    public Order placeSellOrder(){
        Order order = new Order("AAPL", 10.00, Order.OrderType.SELLSTOP, BigDecimal.valueOf(300.22));
        orderProcessor.processOrder(order);
        return order;
    }
}
