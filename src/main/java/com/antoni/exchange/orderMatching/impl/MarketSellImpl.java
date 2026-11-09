package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.controllers.OrderReceiptPublisher;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MarketSellImpl extends MatchingAlgorithm {
    public MarketSellImpl(TickerMap tickerMap, OrderReceiptPublisher orderReceiptPublisher) {
        super(tickerMap, orderReceiptPublisher);
    }

    //Look over, implement recipts, fill order as available then cancel the rest
    public void matchOrder(Order order) {
        while (order.getQuantity() != 0.00) {
            Order highestBid = tickerMap.getHighestBid(order.getTicker());
            if (highestBid != null) {
                //if there is a highestbid
                if (order.getQuantity() < highestBid.getQuantity()) {
                    //The highestbid wants to buy more
                    //Sell and update the highestBid
                    System.out.println("Sold out");
                    highestBid.setQuantity(highestBid.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == highestBid.getQuantity()) {
                    System.out.println("Sold out");
                    tickerMap.removeHighestBid(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The highest bid doesn't buy out the sell order
                    order.setQuantity(order.getQuantity() - highestBid.getQuantity());
                    tickerMap.removeHighestBid(order.getTicker());
                }
            } else {
                //There is no buy order to fulfill this sell order
                addToMap(order);
                return;
            }
        }
    }
}
