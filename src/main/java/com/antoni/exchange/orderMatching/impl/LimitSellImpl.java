package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class LimitSellImpl extends MatchingAlgorithm {

    public LimitSellImpl(TickerMap tickerMap) {
        super(tickerMap);
    }
    public void matchOrder(Order order) {
        while (order.getQuantity() != 0.00) {
            Order highestBid = tickerMap.getHighestBid(order.getTicker());
            if (highestBid != null && order.getPrice().compareTo(highestBid.getPrice()) <= 0) {
                //If the sell limit price is less than or equal to the highestbid
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
                //price is smaller therefore we add to map as we cannot sell
                addToMap(order);
                return;
            }
        }
    }

}
