package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class LimitSellImplTest {
    private TickerMapImpl tickerMap;


    private LimitSellImpl limitSell;

    @BeforeEach
    void setUp() {
        tickerMap = new TickerMapImpl();
        limitSell = new LimitSellImpl(tickerMap);
        LimitBuyImpl limitBuy = new LimitBuyImpl(tickerMap);
        MarketBuyImpl marketBuy = new MarketBuyImpl(tickerMap);
        MarketSellImpl marketSell = new MarketSellImpl(tickerMap);
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
    }
}
