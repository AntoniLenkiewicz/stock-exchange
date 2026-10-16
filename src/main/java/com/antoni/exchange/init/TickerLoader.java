package com.antoni.exchange.init;

import com.antoni.exchange.models.Ticker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class TickerLoader {

    private final ObjectMapper objectMapper;

    private List<Ticker> tickers;

    public TickerLoader (ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //Read tickers and add them to the map
    @Value("${ticker.file}")
    private String tickerFile;

    public void LoadTickers(){
        Resource resource = new ClassPathResource(tickerFile);
        try {
            InputStream inputStream = resource.getInputStream();
            tickers = objectMapper.readValue(inputStream, new TypeReference<List<Ticker>>() {} );
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public List<Ticker> getTicker() {
        return this.tickers;
    }
}
