package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.controllers.BuyController;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class TickerMapImpl implements TickerMap {
    private Map<String, BuyOrderTreeImpl> buyOrders = new HashMap<>();

    private Map<String, SellOrderTreeImpl> sellOrders = new HashMap<>();

    //Need to add functions to get the lowest ask, highest bid etc
    public void addSellOrder(Order sellOrder){
        //Code to add order to the respective order tree
        String ticker = sellOrder.ticker;
        sellOrders.get(ticker).addOrderToTree(sellOrder);
    }

    public void addBuyOrder(Order buyOrder) {
        //Code to add order to the respective order tree
        String ticker = buyOrder.ticker;
        buyOrders.get(ticker).addOrderToTree(buyOrder);
    }

    public SellOrderTreeImpl getSellOrders(String ticker){
        return sellOrders.get(ticker);
    }

    public BuyOrderTreeImpl getBuyOrders(String ticker){
        //Code to return the respective order tree
        return buyOrders.get(ticker);
    }

    public Order getHighestBid(String ticker) {
        return buyOrders.get(ticker).getHighestBid();
    }

    public Order getLowestAsk(String ticker) {
        return sellOrders.get(ticker).getLowestAsk();
    }

    public void addTicker(String ticker) {
        if (buyOrders.get(ticker) == null) {
            BuyOrderTreeImpl buyOrderList = new BuyOrderTreeImpl();
            buyOrders.put(ticker, buyOrderList);
        }
        if (sellOrders.get(ticker) == null) {
            SellOrderTreeImpl sellOrderList = new SellOrderTreeImpl();
            sellOrders.put(ticker, sellOrderList);
        }
    }

    public Set<String> getAllTickers(){
        return buyOrders.keySet();
    }
}
