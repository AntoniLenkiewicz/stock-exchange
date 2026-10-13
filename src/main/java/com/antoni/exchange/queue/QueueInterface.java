package com.antoni.exchange.queue;

import com.antoni.exchange.models.Order;

public interface QueueInterface {
    void addToQueue(Order order) throws InterruptedException;

    Order takeFromQueue() throws InterruptedException;

    Order removeFromQueue();

    boolean isEmpty();
}
