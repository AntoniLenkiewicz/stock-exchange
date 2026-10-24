package com.antoni.exchange.orderStorage;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.impl.BuyOrderTreeImpl;
import com.antoni.exchange.orderStorage.impl.SellOrderTreeImpl;

import java.util.Set;

public interface TickerMap {
    void addSellOrder(Order order);

    Set<String> getAllTickers();

    void addBuyOrder(Order buyOrder);

    SellOrderTreeImpl getSellOrders(String ticker);

    BuyOrderTreeImpl getBuyOrders(String ticker);

    Order getHighestBid(String ticker);

    Order getLowestAsk(String ticker);

    void addTicker(String ticker);

    boolean isTickerPresent(String ticker);

    Order removeLowestAsk(String ticker);

    Order removeHighestBid(String ticker);
}
