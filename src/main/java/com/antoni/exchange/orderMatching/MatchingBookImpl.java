package com.antoni.exchange.orderMatching;

import com.antoni.exchange.models.Order;
import com.antoni.exchange.orderStorage.TickerMap;
import org.springframework.stereotype.Service;

@Service
public class MatchingBookImpl implements MatchingBook {
    //FIFO algorithm, for now treat all orders the same, either buy/sell at certain price
    //Treat price as a limit, fill buy order with lowest price up to the limit
    //Fill sell order with highest bids down to the limit
    private final TickerMap tickerMap;

    public MatchingBookImpl (TickerMap tickerMap) {
        this.tickerMap = tickerMap;
    }

    public void matchOrder(Order order) {
        // Algorithm to match based on price
        //Will have to return a 'receipt' of how the order was filled
        if (this.isBuyOrSell(order).equals("SELL")) {
            matchSellOrder(order);
        } else if (this.isBuyOrSell(order).equals("BUY")) {
            matchBuyOrder(order);
        }
    }
    private void matchBuyOrder(Order order) {
        while (order.getQuantity() != 0.00) {
            Order lowestAsk = tickerMap.getLowestAsk(order.getTicker());
            if (lowestAsk != null && order.getPrice().compareTo(lowestAsk.getPrice()) >= 0) {
                //If the buy limit price is greater than or equal to the lowest ask
                if (order.getQuantity() < lowestAsk.getQuantity()) {
                    //The lowest ask wants to sell more
                    //Sell and update the lowestAsk
                    System.out.println("Bought out");
                    lowestAsk.setQuantity(lowestAsk.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == lowestAsk.getQuantity()) {
                    System.out.println("Bought out");
                    tickerMap.removeLowestAsk(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The lowest ask doesnt fill the buy order completely
                    order.setQuantity(order.getQuantity() - lowestAsk.getQuantity());
                    tickerMap.removeLowestAsk(order.getTicker());
                }
            } else {
                //price is smaller therefore we add to map as we cannot sell
                addToMap(order);
                return;
            }
        }
    }

    private void matchSellOrder(Order order) {
        while (order.getQuantity() != 0.00) {
            Order highestBid = tickerMap.getHighestBid(order.getTicker());
            if (highestBid != null && order.getPrice().compareTo(highestBid.getPrice()) <= 0) {
                //If the sell limit price is less than or equal to the highestbid
                if (order.getQuantity() < highestBid.getQuantity()) {
                    //The highestbid wants to buy more
                    //Sell and update the highestBid
                    System.out.println("Sold out");
                    highestBid.setQuantity(highestBid.getQuantity() - order.getQuantity());
                    order.setQuantity(0.00);
                } else if (order.getQuantity() == highestBid.getQuantity()) {
                    System.out.println("Sold out");
                    tickerMap.removeHighestBid(order.getTicker());
                    order.setQuantity(0.00);
                } else {
                    //The highest bid doesnt buy out the sell order
                    order.setQuantity(order.getQuantity() - highestBid.getQuantity());
                    tickerMap.removeHighestBid(order.getTicker());
                }
            } else {
                //price is smaller therefore we add to map as we cannot sell
                addToMap(order);
                return;
            }
        }
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
        switch (order.getOrderType()) {
            case MARKETBUY, BUYSTOPLIMIT, LIMITBUY, STOPBUY:
                return "BUY";
            case MARKETSELL, SELLSTOPLIMIT, LIMITSELL, STOPSELL:
                return "SELL";
            default:
                return "invalid ordertype";
        }
    }
}
