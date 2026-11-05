package com.antoni.exchange.init;

import com.antoni.exchange.models.Ticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
@SpringBootConfiguration
public class TickerLoaderTest {

    @Value("${ticker.file}")
    public String tickerFile;
    private TestableTickerLoader tickerLoader;

    public class TestableTickerLoader extends TickerLoader {
       public TestableTickerLoader(ObjectMapper objectMapper) {
           super(objectMapper);
       }

       public void setTickerFile(String tickerFile) {
           this.tickerFile = tickerFile;
       }
    }
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        tickerLoader = new TestableTickerLoader(objectMapper);
        tickerLoader.setTickerFile(tickerFile);
    }

    @Test
    void testWrongFilePathWhenOpeningFileThrowsCorrectAssertion() {
        String wrongPath = "./wrongpath/tickers.json";
        String message = "class path resource [wrongpath/tickers.json] cannot be opened because it does not exist";
        try {
            tickerLoader.setTickerFile(wrongPath);
            tickerLoader.LoadTickers();
            fail("Failed to throw exception for wrong file path");
        } catch (IOException e) {
            assertEquals("Exception message doesn't match expected message", message, e.getMessage());
        }
    }

    @Test
    void testTickersLoadedCorrectly() {
        List<Ticker> correctTickerList = new ArrayList<>();
        correctTickerList.add(new Ticker(1, "AAPL"));
        correctTickerList.add(new Ticker(2, "MSFT"));
        correctTickerList.add(new Ticker(3, "GOOGL"));
        try {
            tickerLoader.LoadTickers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Ticker ticker : tickerLoader.getTickers()) {
            assertEquals("Ticker loaded incorrectly", correctTickerList.get(ticker.getId()-1).getName(), ticker.getName());
        }
    }

    @Test
    void testTickersGetReturnsNullIfTickersNotLoaded() {
        assertEquals("Failed to return null", null, tickerLoader.getTickers());
    }
}
