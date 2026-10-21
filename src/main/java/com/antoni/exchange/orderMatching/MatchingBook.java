package com.antoni.exchange.orderMatching;

import com.antoni.exchange.models.Order;

public interface MatchingBook {
    void matchOrder(Order order);
}
