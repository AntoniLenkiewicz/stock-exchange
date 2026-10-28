package com.antoni.exchange.orderStorage.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TickerMapImplTest {
    private TickerMapImpl tickerMap;

    @BeforeEach
    void setUp() {
        tickerMap = new TickerMapImpl();
    }

    @Test
    void testAddTicker() {
        tickerMap.addTicker("NVDA");
        assertTrue(tickerMap.isTickerPresent("NVDA"));
    }
}
