package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.OrderTree;

import java.math.BigDecimal;
import java.util.LinkedList;


public class BuyOrderTreeImpl extends OrderTree {

    public Order getHighestBid() {
        if (orderTree.isEmpty()) {
            return null;
        }
        BigDecimal highestBidPrice = this.orderTree.lastKey();
        LinkedList<Order> orderList = this.orderTree.get(highestBidPrice);
        return orderList.peek();
    }

    public Order removeMostRecentHighestBid() {
        if (orderTree.isEmpty()) {
            return null;
        }
        BigDecimal highestBidPrice = this.orderTree.lastKey();
        LinkedList<Order> orderList = this.orderTree.get(highestBidPrice);
        if (orderList.size() == 1) {
            orderTree.remove(highestBidPrice);
        }
        return orderList.pollFirst();
    }
}
