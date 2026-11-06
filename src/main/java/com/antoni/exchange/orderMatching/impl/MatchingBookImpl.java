package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderMatching.MatchingBook;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MatchingBookImpl implements MatchingBook {
    //FIFO algorithm, for now treat all orders the same, either buy/sell at certain price
    //Treat price as a limit, fill buy order with lowest price up to the limit
    //Fill sell order with highest bids down to the limit

    private final MatchingAlgorithm limitSell;
    private final MatchingAlgorithm limitBuy;
    private final MatchingAlgorithm marketBuy;
    private final MatchingAlgorithm marketSell;

    public MatchingBookImpl (LimitSellImpl limitSell, LimitBuyImpl limitBuy, MarketBuyImpl marketBuy, MarketSellImpl marketSell) {
        this.limitSell = limitSell;
        this.limitBuy = limitBuy;
        this.marketBuy = marketBuy;
        this.marketSell = marketSell;
    }

    public void matchOrder(Order order) {
        // Algorithm to match based on price
        //Will have to return a 'receipt' of how the order was filled
        //Switch to call correct classes impl
        switch (order.getOrderType()) {
            case STOPBUY: break;
            case STOPSELL: break;
            case LIMITSELL: limitSell.matchOrder(order); break;
            case LIMITBUY: limitBuy.matchOrder(order); break;
            case MARKETBUY: marketBuy.matchOrder(order); break;
            case MARKETSELL: marketSell.matchOrder(order); break;
            case BUYSTOPLIMIT: break;
            case SELLSTOPLIMIT: break;
            default: break;
        }
    }
}
