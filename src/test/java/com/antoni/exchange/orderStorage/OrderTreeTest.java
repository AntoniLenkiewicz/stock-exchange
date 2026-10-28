package com.antoni.exchange.orderStorage;

import com.antoni.exchange.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OrderTreeTest {
    private OrderTree orderTree;
    @BeforeEach
    void setUp(){
        orderTree = new OrderTree() {
            public void addOrderToTree(Order order) {
            super.addOrderToTree(order);
        }};
    }

    @Test
    void testAddToOrder() {
        Order order = new Order("AAPL", 20.00, Order.OrderType.MARKETBUY, BigDecimal.valueOf(100.00));
        orderTree.addOrderToTree(order);
        orderTree.addOrderToTree(order);
        LinkedList<Order> orderList = new LinkedList<>();
        orderList.add(order);
        orderList.add(order);
        assertArrayEquals(orderList.toArray(), orderTree.orderTree.get(BigDecimal.valueOf(100.00)).toArray(), "Failed to add orders to order tree");
    }
}
