package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.controllers.OrderReceiptPublisher;
import com.antoni.exchange.controllers.OrderUpdatePublisher;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.models.OrderReceipt;
import com.antoni.exchange.models.SellPrice;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class LimitBuyImpl extends MatchingAlgorithm {

    public LimitBuyImpl(TickerMap tickerMap, OrderReceiptPublisher orderReceiptPublisher) {
        super(tickerMap, orderReceiptPublisher);
    }

    public void matchOrder(Order order) {
        OrderReceipt orderReceipt = new OrderReceipt(order.getOrderId());
        while (order.getQuantity() != 0.00) {
            Order lowestAsk = tickerMap.getLowestAsk(order.getTicker());
            if (lowestAsk != null && order.getPrice().compareTo(lowestAsk.getPrice()) >= 0) {
                OrderReceipt sellerRecipt = new OrderReceipt(lowestAsk.getOrderId());
                //If the buy limit price is greater than or equal to the lowest ask
                if (order.getQuantity() < lowestAsk.getQuantity()) {
                    //The lowest ask wants to sell more
                    //Sell and update the lowestAsk
                    SellPrice sellPrice = new SellPrice(lowestAsk.getPrice(), order.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    lowestAsk.setQuantity(lowestAsk.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == lowestAsk.getQuantity()) {
                    SellPrice sellPrice = new SellPrice(lowestAsk.getPrice(), order.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    sellerRecipt.addSellPrice(sellPrice);
                    tickerMap.removeLowestAsk(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The lowest ask doesn't fill the buy order completely
                    SellPrice sellPrice = new SellPrice(lowestAsk.getPrice(), lowestAsk.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    sellerRecipt.addSellPrice(sellPrice);
                    order.setQuantity(order.getQuantity() - lowestAsk.getQuantity());
                    tickerMap.removeLowestAsk(order.getTicker());
                }
                orderRecieptPublisher.sendReceipt(sellerRecipt);
            } else {
                if (!orderReceipt.getSellPrices().isEmpty()) {
                    orderRecieptPublisher.sendReceipt(orderReceipt);
                }
                //price is smaller therefore we add to map as we cannot sell
                addToMap(order);
                return;
            }
        }
        if (!orderReceipt.getSellPrices().isEmpty()) {
            orderRecieptPublisher.sendReceipt(orderReceipt);
        }
    }
}
