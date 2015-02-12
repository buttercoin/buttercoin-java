package com.buttercoin.api;

import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

public class TestUnauthenticated extends ButtercoinAPITest {
    @Test
    public void testTicker() throws Exception {
        Ticker ticker = buttercoin.getTicker().get();
        assertThat(ticker.getCurrency().equals("USD"));
        assertThat(ticker.getLast().compareTo(BigDecimal.ZERO) > 0);
        assertThat(ticker.getBid().compareTo(BigDecimal.ZERO) > 0);
        assertThat(ticker.getAsk().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testOrderBook() throws Exception {
        OrderBook orderBook = buttercoin.getOrderBook().get();
        assertThat(orderBook.getAsks().size() > 0);
    }

    @Test
    public void testTradeHistory() throws Exception {
        TradeHistory tradeHistory = buttercoin.getTradeHistory().get();
        assertThat(tradeHistory.getTrades().size() == 100);
    }
}
