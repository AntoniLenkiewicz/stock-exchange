package com.antoni.exchange.models;

import java.math.BigDecimal;

public class Order{
    public String ticker;
    public double quantity;
    public enum OrderType {
        MARKETBUY,
        MARKETSELL,
        LIMITSELL,
        LIMITBUY,
        STOPBUY,
        STOPSELL,
        BUYSTOP,
        SELLSTOP
    }
    public OrderType orderType;
    public BigDecimal price;

    public Order(String ticker, double quantity, OrderType orderType, BigDecimal price){
        this.ticker = ticker;
        this.quantity = quantity;
        this.orderType = orderType;
        this.price = price;

    }
}
