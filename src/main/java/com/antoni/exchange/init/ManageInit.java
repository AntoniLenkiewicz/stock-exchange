package com.antoni.exchange.init;

import com.antoni.exchange.models.Ticker;
import com.antoni.exchange.orderStorage.TickerMap;
import com.antoni.exchange.orderStorage.impl.TickerMapImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ManageInit {

    private TickerLoader tickerLoader;

    private TickerMap tickerMap;

    public ManageInit(TickerLoader tickerLoader, TickerMap tickerMap) {
        this.tickerLoader = tickerLoader;
        this.tickerMap = tickerMap;
    }

    @PostConstruct
    public void run() {
        this.initTickers();
        //Code to read snapshot and then to read the wal and simulate past trades
    }

    private void initTickers(){
        try {
            tickerLoader.LoadTickers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Ticker ticker : tickerLoader.getTickers()) {
            tickerMap.addTicker(ticker.getName());
        }
    }
}
