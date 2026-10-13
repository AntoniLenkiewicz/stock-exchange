package com.antoni.exchange.services.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.queue.QueueInterface;
import com.antoni.exchange.services.OrderProcessor;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorImpl implements OrderProcessor {
    private final QueueInterface queue;

    public OrderProcessorImpl(QueueInterface queue) {
        this.queue = queue;
    }
    public void processOrder(Order order) {
        System.out.println("Adding Order to queue");
        try {
            queue.addToQueue(order);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
