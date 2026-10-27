package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.OrderTree;

import java.math.BigDecimal;
import java.util.LinkedList;

public class SellOrderTreeImpl extends OrderTree {
    public Order getLowestAsk(){
        //Figure out how to handle an empty tree/linkedlist
        if (orderTree.isEmpty()) {
            return null;
        }
        BigDecimal lowestAsk = this.orderTree.firstKey();
        LinkedList<Order> orderList = this.orderTree.get(lowestAsk);
        return orderList.peek();
    }

    public Order removeMostRecentLowestAsk(){
        if (orderTree.isEmpty()) {
            return null;
        }
        BigDecimal lowestAsk = this.orderTree.firstKey();
        LinkedList<Order> orderList = this.orderTree.get(lowestAsk);
        if (orderList.size() == 1) {
            orderTree.remove(lowestAsk);
        }
        return orderList.pollFirst();
    }
}
