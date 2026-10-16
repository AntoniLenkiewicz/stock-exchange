package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.models.Ticker;
import com.antoni.exchange.orderStorage.TickerMap;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import com.antoni.exchange.queue.queueServices.OrderProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
public class BuyController {

    private final OrderProcessor orderProcessor;
    private final TickerMap tickerMap;

    public BuyController(OrderProcessor orderProcessor, TickerMap tickerMap) {
        this.orderProcessor = orderProcessor;
        this.tickerMap = tickerMap;
    }

    @GetMapping("/buy")
    public Order placeBuyOrder() {
        Order order = new Order("AAPL", 10.00, Order.OrderType.SELLSTOP, BigDecimal.valueOf(300.22));
        orderProcessor.processOrder(order);
        return order;
    }
    @GetMapping("/tickers")
    public Set<String> getTickers() {
        return tickerMap.getAllTickers();
    }
}
