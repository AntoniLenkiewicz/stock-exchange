package com.antoni.exchange.orderMatching;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;

public abstract class MatchingAlgorithm {
    protected TickerMap tickerMap;

    public MatchingAlgorithm(TickerMap tickerMap) {
        this.tickerMap = tickerMap;
    }


    public abstract void matchOrder(Order order);

    protected void addToMap(Order order) {
        if (this.isBuyOrSell(order).equals("SELL")) {
            tickerMap.addSellOrder(order);
        } else if (this.isBuyOrSell(order).equals("BUY")) {
            tickerMap.addBuyOrder(order);
        }
    }
    protected String isBuyOrSell(Order order) {
        switch (order.getOrderType()) {
            case MARKETBUY, BUYSTOPLIMIT, LIMITBUY, STOPBUY:
                return "BUY";
            case MARKETSELL, SELLSTOPLIMIT, LIMITSELL, STOPSELL:
                return "SELL";
            default:
                return "invalid ordertype";
        }
    }
}
