package com.antoni.exchange.models;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderReceipt {
    private static int id = 0;

    public OrderReceipt(int orderId) {
        this.orderId = orderId;
        this.orderRecieptId = id++;
    }
    @NotNull
    public int orderRecieptId;
    @NotNull
    public int orderId;

    private List<SellPrice> sellPrices = new ArrayList<>();

    public void addSellPrice(SellPrice sellPrice) {
        sellPrices.add(sellPrice);
    }

    public List<SellPrice> getSellPrices(){
        return this.sellPrices;
    }

    public String toString() {
        String sellPricesString = ("{\"sellPrices\":" + sellPrices + "}");
        return sellPricesString;
    }
}
