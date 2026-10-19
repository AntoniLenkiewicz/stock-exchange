package com.antoni.exchange.queue.queueServices.impl;

import com.antoni.exchange.models.Order;
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

    private final TickerMap tickerMap;

    private final WriteAheadLogger writeAheadLogger;

    public QueueProcessorImpl(QueueInterface queue, TickerMap tickerMap, WriteAheadLogger writeAheadLogger) {
        this.queue = queue;
        this.tickerMap = tickerMap;
        this.writeAheadLogger = writeAheadLogger;
    }

    @PostConstruct
    public void run() {
        Thread processor = new Thread(() -> {
            while (true) {
                try {
                    Order order = queue.takeFromQueue();
                    writeAheadLogger.writeLog(order.toString());
                    this.addToMap(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        processor.setName("processor-thread");
        processor.start();
    }

    private void addToMap(Order order) {
        if (this.isBuyOrSell(order).equals("SELL")) {
            tickerMap.addSellOrder(order);
        } else if (this.isBuyOrSell(order).equals("BUY")) {
            tickerMap.addBuyOrder(order);
        } else {
            return; // Code to handle wrong ordertype
        }
    }
    private String isBuyOrSell(Order order) {
        switch (order.orderType) {
            case MARKETBUY, BUYSTOPLIMIT, LIMITBUY, STOPBUY:
                return "BUY";
            case MARKETSELL, SELLSTOPLIMIT, LIMITSELL, STOPSELL:
                return "SELL";
            default:
                return "invalid ordertype";
        }
    }
}

