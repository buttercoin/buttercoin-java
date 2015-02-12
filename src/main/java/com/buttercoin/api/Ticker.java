package com.buttercoin.api;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class Ticker {
    private String currency;
    private BigDecimal last;
    private BigDecimal bid;
    private BigDecimal ask;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticker ticker = (Ticker) o;

        if (ask != null ? !ask.equals(ticker.ask) : ticker.ask != null) return false;
        if (bid != null ? !bid.equals(ticker.bid) : ticker.bid != null) return false;
        if (currency != null ? !currency.equals(ticker.currency) : ticker.currency != null) return false;
        if (last != null ? !last.equals(ticker.last) : ticker.last != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (last != null ? last.hashCode() : 0);
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (ask != null ? ask.hashCode() : 0);
        return result;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getLast() {
        return last;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("currency", currency)
                .add("last", last.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .add("bid", bid.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .add("ask", ask.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .toString();
    }
}
