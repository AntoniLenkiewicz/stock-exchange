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
    protected String tickerFile;

    public void LoadTickers() throws IOException{
        Resource resource = new ClassPathResource(tickerFile);
        InputStream inputStream = resource.getInputStream();
        tickers = objectMapper.readValue(inputStream, new TypeReference<>() {} );
    }

    public List<Ticker> getTickers() {
        return this.tickers;
    }
}
