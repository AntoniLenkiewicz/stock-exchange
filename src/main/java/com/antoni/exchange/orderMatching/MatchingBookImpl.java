package com.antoni.exchange.orderMatching;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MatchingBookImpl implements MatchingBook {
    //FIFO algorithm, for now treat all orders the same, either buy/sell at certain price
    //Treat price as a limit, fill buy order with lowest price up to the limit
    //Fill sell order with highest bids down to the limit
    private final TickerMap tickerMap;

    public MatchingBookImpl (TickerMap tickerMap) {
        this.tickerMap = tickerMap;
    }

    public void matchOrder(Order order) {
        // Algorithm to match based on price
        //Will have to return a 'receipt' of how the order was filled
        addToMap(order);
    }
    private void addToMap(Order order) {
        if (this.isBuyOrSell(order).equals("SELL")) {
            tickerMap.addSellOrder(order);
        } else if (this.isBuyOrSell(order).equals("BUY")) {
            tickerMap.addBuyOrder(order);
        } else {
            return; // Code to handle wrong ordertype
        }
    }
    private String isBuyOrSell(Order order) {
        switch (order.orderType) {
            case MARKETBUY, BUYSTOPLIMIT, LIMITBUY, STOPBUY:
                return "BUY";
            case MARKETSELL, SELLSTOPLIMIT, LIMITSELL, STOPSELL:
                return "SELL";
            default:
                return "invalid ordertype";
        }
    }
}
