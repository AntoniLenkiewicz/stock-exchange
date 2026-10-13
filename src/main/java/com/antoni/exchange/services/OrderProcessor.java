package com.antoni.exchange.services;

import com.antoni.exchange.models.Order;

public interface OrderProcessor {
    void processOrder(Order order);
}
