package com.buttercoin.api;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import java.util.Iterator;
import java.util.List;

public class TradeHistory implements Iterable<Trade> {
    private List<Trade> trades;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradeHistory that = (TradeHistory) o;

        return !(trades != null ? !trades.equals(that.trades) : that.trades != null);
    }

    @Override
    public int hashCode() {
        return trades != null ? trades.hashCode() : 0;
    }

    @Override
    public Iterator<Trade> iterator() {
        return trades != null ? trades.iterator() : ImmutableSet.<Trade>of().iterator();
    }

    public List<Trade> getTrades() {
        return trades;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("trades", trades).toString();
    }
}