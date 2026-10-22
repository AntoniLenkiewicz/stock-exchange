package com.antoni.exchange.models;

import jakarta.validation.constraints.NotNull;

public class OrderStatus {
    @NotNull
    private int id;
    @NotNull
    private String ticker;
    public enum Status {
        RECIEVED,
        ACCEPTED,
        DECLINED,
        CANCELLED
    }
    @NotNull
    private Status  orderStatus;

    public OrderStatus(int id, String ticker, Status orderStatus) {
        this.id = id;
        this.ticker = ticker;
        this.orderStatus = orderStatus;
    }

    public Status getOrderStatus() {
        return this.orderStatus;
    }
}
