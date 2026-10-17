package com.antoni.exchange.controllers;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class GetController {

    private final TickerMap tickerMap;

    public GetController(TickerMap tickerMap) {
        this.tickerMap = tickerMap;
    }

    @GetMapping("/lowest-ask/{ticker}")
    public String getLowestAsk(@PathVariable String ticker) {
        Order order = tickerMap.getLowestAsk(ticker);
        return order.price.toString().toUpperCase();
    }

    @GetMapping("/tickers")
    public Set<String> getTickers() {
        return tickerMap.getAllTickers();
    }
}
