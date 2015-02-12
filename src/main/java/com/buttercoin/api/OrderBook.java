package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.List;

public class OrderBook {
    @JsonProperty("bid")
    private List<SimpleOrder> bids;

    @JsonProperty("ask")
    private List<SimpleOrder> asks;

    private String priceCurrency;
    private String quantityCurrency;

    public List<SimpleOrder> getBids() {
        return bids;
    }

    public List<SimpleOrder> getAsks() {
        return asks;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getQuantityCurrency() {
        return quantityCurrency;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bids", bids)
                .add("asks", asks)
                .add("priceCurrency", priceCurrency)
                .add("quantityCurrency", quantityCurrency)
                .toString();
    }
}