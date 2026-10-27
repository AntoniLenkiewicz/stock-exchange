package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class SellOrderTreeImplTest {
    private SellOrderTreeImpl sellOrderTree;

    private Order lowPriceOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));

    private Order secondLowPriceOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));

    private Order midPriceOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(200.00));

    private Order highPriceOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(300.00));

    @BeforeEach
    void setUp() {
        sellOrderTree = new SellOrderTreeImpl();
    }


    @Test
    void testLowestAskRetrival() {
        sellOrderTree.addOrderToTree(lowPriceOrder);
        sellOrderTree.addOrderToTree(highPriceOrder);
        sellOrderTree.addOrderToTree(midPriceOrder);
        assertEquals( "Failed to retrieve lowest ask", lowPriceOrder, sellOrderTree.getLowestAsk());
    }

    @Test
    void testLowestAskRemoval() {
        sellOrderTree.addOrderToTree(lowPriceOrder);
        sellOrderTree.addOrderToTree(highPriceOrder);
        sellOrderTree.addOrderToTree(midPriceOrder);
        assertEquals( "Failed to retrieve lowest ask", lowPriceOrder, sellOrderTree.removeMostRecentLowestAsk());
    }

    @Test
    void testOrderedLowestAskRemoval() {
        sellOrderTree.addOrderToTree(lowPriceOrder);
        sellOrderTree.addOrderToTree(highPriceOrder);
        sellOrderTree.addOrderToTree(secondLowPriceOrder);
        sellOrderTree.addOrderToTree(midPriceOrder);
        assertAll("Ordered Lowest Ask Removal",
                () -> assertEquals("Failed to remove lowest ask", lowPriceOrder, sellOrderTree.removeMostRecentLowestAsk()),
                () -> assertEquals("Failed to remove second copy of lowest ask", secondLowPriceOrder, sellOrderTree.removeMostRecentLowestAsk()),
                () -> assertEquals("Failed to remove next lowest ask", midPriceOrder, sellOrderTree.removeMostRecentLowestAsk())
        );
    }
}
