package com.antoni.exchange.queue;

import com.antoni.exchange.models.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class QueueImpl implements QueueInterface {
     private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

     public void addToQueue(Order order) throws InterruptedException {
        orderQueue.put(order);
    }

     public Order removeFromQueue(){
        return orderQueue.poll();
    }

     public boolean isEmpty() {
        return orderQueue.isEmpty();
    }
     public Order takeFromQueue() throws InterruptedException{
         return orderQueue.take();
     }

}
