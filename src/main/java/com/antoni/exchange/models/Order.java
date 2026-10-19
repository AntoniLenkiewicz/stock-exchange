package com.antoni.exchange.models;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class Order{
    @NotNull
    public String ticker;
    @NotNull
    public double quantity;
    public enum OrderType {
        MARKETBUY,
        MARKETSELL,
        LIMITSELL,
        LIMITBUY,
        STOPBUY,
        STOPSELL,
        BUYSTOPLIMIT,
        SELLSTOPLIMIT
    }
    @NotNull
    public OrderType orderType;
    @DecimalMin(value = "0.0", inclusive = false)
    public BigDecimal price;

    public Order(String ticker, double quantity, OrderType orderType, BigDecimal price){
        this.ticker = ticker;
        this.quantity = quantity;
        this.orderType = orderType;
        this.price = price;
    }

    @Override
    public String toString(){
        StringBuilder orderString = new StringBuilder();
        orderString.append("{\"ticker:\"");
        orderString.append(ticker);
        orderString.append("\",\"quantity\":");
        orderString.append(quantity);
        orderString.append(",\"orderType\":\"");
        orderString.append(orderType.toString());
        orderString.append("\",\"price\":");
        orderString.append(price.toString());
        orderString.append("}");
        return orderString.toString();
    }
}
