package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.TestMessageChannel;
import com.antoni.exchange.controllers.OrderReceiptPublisher;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class LimitBuyImplTest {
    private TickerMapImpl tickerMap;

    private LimitBuyImpl limitBuy;

    @BeforeEach
    void setUp() {
        SimpMessagingTemplate messagingTemplate = new SimpMessagingTemplate(new TestMessageChannel());
        OrderReceiptPublisher orderReceiptPublisher = new OrderReceiptPublisher(messagingTemplate);
        tickerMap = new TickerMapImpl();
        limitBuy = new LimitBuyImpl(tickerMap, orderReceiptPublisher);
    }

    @Test
    void testLimitBuyWithAlignedSellOrder() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addSellOrder(sellOrder);

        limitBuy.matchOrder(buyOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty())
        );
    }

    @Test
    void testLimitBuyWithHigherQuantityThanSell() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addSellOrder(sellOrder);

        limitBuy.matchOrder(buyOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
    }

    @Test
    void testLimitBuyWithLowerQuantityThanSell() {
        Order buyOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addSellOrder(sellOrder);

        limitBuy.matchOrder(buyOrder);
        assertAll(
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid())
        );
    }
    @Test
    void testLimitBuyWithHigherPriceThanSell() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(200.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addSellOrder(sellOrder);

        limitBuy.matchOrder(buyOrder);
        assertAll(
                () -> assertEquals("Buy order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
    }

    @Test
    void testLimitBuyWithLowerPriceThanSell() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(200.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addSellOrder(sellOrder);

        limitBuy.matchOrder(buyOrder);
        assertAll(
                () -> assertTrue("Buy order reference is not the same as original object",
                        tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order reference is not the same as original object",
                        tickerMap.getSellOrders("AAPL").isEmpty())
        );
    }
}
