package com.antoni.exchange.orderMatching.impl;

import com.antoni.exchange.controllers.OrderReceiptPublisher;
import com.antoni.exchange.models.Order;
import com.antoni.exchange.models.OrderReceipt;
import com.antoni.exchange.models.SellPrice;
import com.antoni.exchange.orderMatching.MatchingAlgorithm;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MarketSellImpl extends MatchingAlgorithm {
    public MarketSellImpl(TickerMap tickerMap, OrderReceiptPublisher orderReceiptPublisher) {
        super(tickerMap, orderReceiptPublisher);
    }

    //Look over, implement recipts, fill order as available then cancel the rest
    public void matchOrder(Order order) {
        OrderReceipt orderReceipt = new OrderReceipt(order.getOrderId());
        while (order.getQuantity() != 0.00) {
            Order highestBid = tickerMap.getHighestBid(order.getTicker());
            if (highestBid != null) {
                OrderReceipt buyerRecipt = new OrderReceipt(highestBid.getOrderId());
                //if there is a highestbid
                if (order.getQuantity() < highestBid.getQuantity()) {
                    //The highestbid wants to buy more
                    //Sell and update the highestBid
                    SellPrice sellPrice = new SellPrice(highestBid.getPrice(), order.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    highestBid.setQuantity(highestBid.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == highestBid.getQuantity()) {
                    SellPrice sellPrice = new SellPrice(highestBid.getPrice(), order.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    buyerRecipt.addSellPrice(sellPrice);
                    tickerMap.removeHighestBid(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The highest bid doesn't buy out the sell order
                    SellPrice sellPrice = new SellPrice(highestBid.getPrice(), highestBid.getQuantity());
                    orderReceipt.addSellPrice(sellPrice);
                    buyerRecipt.addSellPrice(sellPrice);
                    order.setQuantity(order.getQuantity() - highestBid.getQuantity());
                    tickerMap.removeHighestBid(order.getTicker());
                }
                orderRecieptPublisher.sendReceipt(buyerRecipt);
            } else {
                //There is no buy order to fulfill this sell order
                if (!orderReceipt.getSellPrices().isEmpty()) {
                    orderRecieptPublisher.sendReceipt(orderReceipt);
                }
                addToMap(order);
                return;
            }
        }
        if (!orderReceipt.getSellPrices().isEmpty()) {
            orderRecieptPublisher.sendReceipt(orderReceipt);
        }
    }
}
