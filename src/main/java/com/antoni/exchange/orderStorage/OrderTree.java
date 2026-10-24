package com.antoni.exchange.orderStorage;

import com.antoni.exchange.models.Order;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class OrderTree {
    protected SortedMap<BigDecimal, LinkedList<Order>> orderTree = new TreeMap<>();

    public void addOrderToTree(Order order) {
        final BigDecimal price = order.getPrice();
        LinkedList<Order> orderList = this.orderTree.get(price);
        if (orderList == null) {
            orderList = new LinkedList<>();
        }
        orderList.add(order);
        orderTree.put(price, orderList);
    }
}
