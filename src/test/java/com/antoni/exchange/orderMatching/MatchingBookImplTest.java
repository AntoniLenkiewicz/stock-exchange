package com.antoni.exchange.orderMatching;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.util.AssertionErrors.*;

class MatchingBookImplTest {

    private TickerMapImpl tickerMap;

    private MatchingBook matchingBook;

    @BeforeEach
    void setUp() {
        tickerMap = new TickerMapImpl();
        matchingBook = new MatchingBookImpl(tickerMap);
    }

    @Test
    void testMatchWithAlignedOrders() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        matchingBook.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty())
        );
    }

    @Test
    void testMatchWithHigherSellQuantityThanBuy() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        matchingBook.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order is still in tree", tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
    }

    @Test
    void testMatchWithHigherBuyQuantityThanSell() {
        Order buyOrder = new Order("AAPL", 40.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        matchingBook.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Sell order is still in tree", tickerMap.getSellOrders("AAPL").isEmpty()),
                () -> assertEquals("Sell order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid())
        );
    }
    @Test
    void testMatchWithHigherSellPriceThanBuy() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(100.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(200.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        matchingBook.matchOrder(sellOrder);
        assertAll(
                () -> assertEquals("Buy order reference is not the same as original object", buyOrder,
                        tickerMap.getBuyOrders("AAPL").getHighestBid()),
                () -> assertEquals("Sell order reference is not the same as original object", sellOrder,
                        tickerMap.getSellOrders("AAPL").getLowestAsk())
        );
    }

    @Test
    void testMatchWithHigherBuyPriceThanSell() {
        Order buyOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITBUY, BigDecimal.valueOf(200.00));
        Order sellOrder = new Order("AAPL", 20.00, Order.OrderType.LIMITSELL, BigDecimal.valueOf(100.00));
        tickerMap.addTicker("AAPL");
        tickerMap.addBuyOrder(buyOrder);

        matchingBook.matchOrder(sellOrder);
        assertAll(
                () -> assertTrue("Buy order reference is not the same as original object",
                        tickerMap.getBuyOrders("AAPL").isEmpty()),
                () -> assertTrue("Sell order reference is not the same as original object",
                        tickerMap.getSellOrders("AAPL").isEmpty())
        );
    }

}
