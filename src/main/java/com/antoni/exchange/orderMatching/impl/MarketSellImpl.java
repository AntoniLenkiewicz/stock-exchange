package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MarketSellImpl extends MatchingAlgorithm {
    public MarketSellImpl(TickerMap tickerMap) {
        super(tickerMap);
    }
    public void matchOrder(Order order) {

    }
}
