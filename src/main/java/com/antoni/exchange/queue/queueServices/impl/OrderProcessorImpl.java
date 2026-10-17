package com.antoni.exchange.queue.queueServices.impl;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;
import com.antoni.exchange.queue.QueueInterface;
import com.antoni.exchange.queue.queueServices.OrderProcessor;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessorImpl implements OrderProcessor {
    private final QueueInterface queue;

    private final TickerMap tickerMap;

    public OrderProcessorImpl(QueueInterface queue, TickerMap tickerMap) {
        this.queue = queue;
        this.tickerMap = tickerMap;
    }

    public void processOrder(Order order) {
        System.out.println("Adding Order to queue");
        try {
            if (tickerMap.isTickerPresent(order.ticker)) {
                queue.addToQueue(order);
            } else {
                return;
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
