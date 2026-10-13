package com.antoni.exchange.orderStorage.impl;

import com.antoni.exchange.controllers.BuyController;
import com.antoni.exchange.models.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TickerMapImpl {
    private Map<String, BuyOrderTreeImpl> buyOrders = new HashMap<>();

    private Map<String, SellOrderTreeImpl> sellOrders = new HashMap<>();

    //Helper functions to simply add/remove from the map
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
}
