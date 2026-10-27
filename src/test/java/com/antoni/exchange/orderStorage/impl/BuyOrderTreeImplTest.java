package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class BuyOrderTreeImplTest {

    private BuyOrderTreeImpl buyOrderTree;

    private Order lowPriceOrder = new Order("AAPL", 20.00, Order.OrderType.MARKETBUY, BigDecimal.valueOf(100.00));

    private Order midPriceOrder = new Order("AAPL", 20.00, Order.OrderType.MARKETBUY, BigDecimal.valueOf(200.00));

    private Order highPriceOrder = new Order("AAPL", 20.00, Order.OrderType.MARKETBUY, BigDecimal.valueOf(300.00));

    private Order secondHighPriceOrder = new Order("AAPL", 20.00, Order.OrderType.MARKETBUY, BigDecimal.valueOf(300.00));

    @BeforeEach
    void setUpTestClass() {
        buyOrderTree = new BuyOrderTreeImpl();

    }

    @Test
    void testHighestBidRetrival() {
        buyOrderTree.addOrderToTree(lowPriceOrder);
        buyOrderTree.addOrderToTree(highPriceOrder);
        buyOrderTree.addOrderToTree(midPriceOrder);
        assertEquals( "Failed to retrieve highest bid",highPriceOrder, buyOrderTree.getHighestBid());
    }

    @Test
    void testHighestBidRemoval() {
        buyOrderTree.addOrderToTree(lowPriceOrder);
        buyOrderTree.addOrderToTree(highPriceOrder);
        buyOrderTree.addOrderToTree(midPriceOrder);
        assertEquals("Failed to remove Highest bid", highPriceOrder, buyOrderTree.removeMostRecentHighestBid());
    }

    @Test
    void testOrderedHighestBidRemoval() {
        buyOrderTree.addOrderToTree(lowPriceOrder);
        buyOrderTree.addOrderToTree(highPriceOrder);
        buyOrderTree.addOrderToTree(secondHighPriceOrder);
        buyOrderTree.addOrderToTree(midPriceOrder);
        assertAll("Ordered Highest Bid Removal",
                () -> assertEquals("Failed to remove highest bid", highPriceOrder, buyOrderTree.removeMostRecentHighestBid()),
                () -> assertEquals("Failed to remove second copy of highest bid", secondHighPriceOrder, buyOrderTree.removeMostRecentHighestBid()),
                () -> assertEquals("Failed to remove next highest bid", midPriceOrder, buyOrderTree.removeMostRecentHighestBid())
        );
    }
}
