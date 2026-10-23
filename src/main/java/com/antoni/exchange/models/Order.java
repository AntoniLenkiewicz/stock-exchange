package com.antoni.exchange.models;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class Order{
    private static int id = 0;
    @NotNull
    private int orderId;
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
    public enum Status {
        RECIEVED,
        ACCEPTED,
        DECLINED,
        CANCELLED
    }
    @NotNull
    public OrderType orderType;
    @NotNull
    public Status orderStatus;
    @DecimalMin(value = "0.0", inclusive = false)
    public BigDecimal price;

    public Order(String ticker, double quantity, OrderType orderType, BigDecimal price){
        this.orderId = id++;
        this.ticker = ticker;
        this.quantity = quantity;
        this.orderType = orderType;
        this.price = price;
        this.orderStatus = Status.RECIEVED;
    }

    public void setOrderStatus(Order.Status orderStatus){
        this.orderStatus = orderStatus;
    }

    public int getOrderId(){
        return this.orderId;
    }

    @Override
    public String toString(){
        StringBuilder orderString = new StringBuilder();
        orderString.append("{\"id\"");
        orderString.append(orderId);
        orderString.append(",\"ticker:\"");
        orderString.append(ticker);
        orderString.append("\",\"quantity\":");
        orderString.append(quantity);
        orderString.append(",\"orderType\":\"");
        orderString.append(orderType.toString());
        orderString.append("\",\"price\":");
        orderString.append(price.toString());
        orderString.append(", \"orderStatus\", \"");
        orderString.append(orderStatus);
        orderString.append("\"}");
        return orderString.toString();
    }
}
