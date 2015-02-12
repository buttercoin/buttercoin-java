package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class AccountBalances {
    @JsonProperty("BTC")
    private BigDecimal btc;

    @JsonProperty("USD")
    private BigDecimal usd;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountBalances that = (AccountBalances) o;

        if (btc != null ? !btc.equals(that.btc) : that.btc != null) return false;
        if (usd != null ? !usd.equals(that.usd) : that.usd != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = btc != null ? btc.hashCode() : 0;
        result = 31 * result + (usd != null ? usd.hashCode() : 0);
        return result;
    }

    public BigDecimal getBtc() {
        return btc;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("btc", btc.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString())
                .add("usd", usd.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .toString();
    }
}