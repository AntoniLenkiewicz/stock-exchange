package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.controllers.OrderReceiptPublisher;
import com.antoni.exchange.models.OrderReceipt;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class TestableOrderReciptPublisher extends OrderReceiptPublisher {

    List<OrderReceipt> receipts = new ArrayList<>();

    public TestableOrderReciptPublisher(SimpMessagingTemplate simpMessagingTemplate) {
        super(simpMessagingTemplate);
    }

    @Override
    public void sendReceipt(OrderReceipt receipt) {
        receipts.add(receipt);
    }

    public String getReceipts() {
        return receipts.toString();
    }
}
