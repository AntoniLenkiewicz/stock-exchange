package com.antoni.exchange.queue.queueServices;

import com.antoni.exchange.models.Order;

public interface OrderProcessor {
    void processOrder(Order order);
}
