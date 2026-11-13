package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.TestableMessageChannel;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootConfiguration
@SpringBootTest
public class LimitSellImplTest {
    private TickerMapImpl tickerMap;

    private LimitSellImpl limitSell;

    private TestableOrderReciptPublisher orderReceiptPublisher;

    @BeforeEach
    void setUp() {
        SimpMessagingTemplate messagingTemplate = new SimpMessagingTemplate(new TestableMessageChannel());
        orderReceiptPublisher = new TestableOrderReciptPublisher(messagingTemplate);
        tickerMap = new TickerMapImpl();
        limitSell = new LimitSellImpl(tickerMap, orderReceiptPublisher);
    }

    @Test
    void testLimitSellWithAlignedBuyOrder() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        limitSell.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty())
        );
        assertEquals("Receipts do not match for aligned limit sell and buy", "[{\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}, {\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}]",
                orderReceiptPublisher.getReceipts());
    }

    @Test
    void testLimitSellWithHigherQuantityThanBuy() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        limitSell.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
        assertEquals("Receipts do not match for limit sell with higher quantity than buy", "[{\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}, {\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}]",
                orderReceiptPublisher.getReceipts());
    }

    @Test
    void testLimitSellWithLowerQuantityThanBuy() {
        Order buyOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        limitSell.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid())
        );
        assertEquals("Receipts do not match for limit sell with lower quantity than buy", "[{\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}, {\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}]",
                orderReceiptPublisher.getReceipts());
    }
    @Test
    void testLimitSellWithHigherPriceThanBuy() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(200.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        limitSell.matchOrder(sellOrder);
        assertAll(
                () -> assertEquals("Buy order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
        assertEquals("Receipts are not empty, for limit sell with higher price than buy", "[]",
                orderReceiptPublisher.getReceipts());
    }

    @Test
    void testLimitSellWithLowerPriceThanBuy() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(200.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        limitSell.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order reference is not the same as original object",
                        tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order reference is not the same as original object",
                        tickerMap.getSellOrders("AAPL").isEmpty())
        );
        assertEquals("Receipts do not match for limit sell with lower price than buy", "[{\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}, {\"sellPrices\":[{\"price\":100.0,\"quantity\":20.0}]}]",
                orderReceiptPublisher.getReceipts());
    }
}
