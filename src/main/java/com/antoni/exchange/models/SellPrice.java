package com.antoni.exchange.models;

import java.math.BigDecimal;

public class SellPrice {
    private double quantity;
    private BigDecimal price;

    public SellPrice(BigDecimal price, double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }
    @Override
    public String toString(){
        return ("{\"price\":" + price.toString() + ",\"quantity\":" + quantity);
    }
}