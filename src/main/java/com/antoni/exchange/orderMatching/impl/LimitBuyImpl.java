package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class LimitBuyImpl extends MatchingAlgorithm {

    public LimitBuyImpl(TickerMap tickerMap) {
        super(tickerMap);
    }

    public void matchOrder(Order order) {
        while (order.getQuantity() != 0.00) {
            Order lowestAsk = tickerMap.getLowestAsk(order.getTicker());
            if (lowestAsk != null && order.getPrice().compareTo(lowestAsk.getPrice()) >= 0) {
                //If the buy limit price is greater than or equal to the lowest ask
                if (order.getQuantity() < lowestAsk.getQuantity()) {
                    //The lowest ask wants to sell more
                    //Sell and update the lowestAsk
                    System.out.println("Bought out");
                    lowestAsk.setQuantity(lowestAsk.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == lowestAsk.getQuantity()) {
                    System.out.println("Bought out");
                    tickerMap.removeLowestAsk(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The lowest ask doesn't fill the buy order completely
                    order.setQuantity(order.getQuantity() - lowestAsk.getQuantity());
                    tickerMap.removeLowestAsk(order.getTicker());
                }
            } else {
                //price is smaller therefore we add to map as we cannot sell
                addToMap(order);
                return;
            }
        }
    }
}
