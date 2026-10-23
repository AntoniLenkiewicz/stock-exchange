package com.antoni.exchange.queue.queueServices.impl;

import com.antoni.exchange.controllers.OrderUpdatePublisher;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderMatching.MatchingBook;
import com.antoni.exchange.orderStorage.TickerMap;
import com.antoni.exchange.queue.QueueInterface;
import com.antoni.exchange.queue.queueServices.QueueProcessor;
import com.antoni.exchange.writeAheadLogger.WriteAheadLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class QueueProcessorImpl implements QueueProcessor {
    //Code to process Queue
    private final QueueInterface queue;

    private final WriteAheadLogger writeAheadLogger;

    private final MatchingBook matchingBook;

    private final OrderUpdatePublisher orderUpdatePublisher;

    public QueueProcessorImpl(QueueInterface queue, WriteAheadLogger writeAheadLogger, MatchingBook matchingBook, OrderUpdatePublisher orderUpdatePublisher) {
        this.queue = queue;
        this.writeAheadLogger = writeAheadLogger;
        this.matchingBook = matchingBook;
        this.orderUpdatePublisher = orderUpdatePublisher;
    }

    @PostConstruct
    public void run() {
        Thread processor = new Thread(() -> {
            while (true) {
                try {
                    Order order = queue.takeFromQueue();
                    writeAheadLogger.writeLog(order.toString());
                    order.setOrderStatus(Order.Status.ACCEPTED);
                    orderUpdatePublisher.sendOrderUpdate(order);
                    matchingBook.matchOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        processor.setName("processor-thread");
        processor.start();
    }


}

