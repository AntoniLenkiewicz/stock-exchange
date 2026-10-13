package com.antoni.exchange.services.impl;

import com.antoni.exchange.queue.QueueInterface;
import com.antoni.exchange.services.QueueProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class QueueProcessorImpl implements QueueProcessor {
    //Code to process Queue
    private final QueueInterface queue;

    public QueueProcessorImpl(QueueInterface queue) {
        this.queue = queue;
    }

    @PostConstruct
    public void run() {
        Thread processor = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(queue.takeFromQueue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        processor.setName("processor-thread");
        processor.start();
    }
}

