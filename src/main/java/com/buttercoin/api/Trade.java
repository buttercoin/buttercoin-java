package com.buttercoin.api;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class Trade {
    private BigDecimal spent;
    private String spentCurrency;
    private BigDecimal earned;
    private String earnedCurrency;
    private long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (timestamp != trade.timestamp) return false;
        if (earned != null ? !earned.equals(trade.earned) : trade.earned != null) return false;
        if (earnedCurrency != null ? !earnedCurrency.equals(trade.earnedCurrency) : trade.earnedCurrency != null)
            return false;
        if (spent != null ? !spent.equals(trade.spent) : trade.spent != null) return false;
        if (spentCurrency != null ? !spentCurrency.equals(trade.spentCurrency) : trade.spentCurrency != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = spent != null ? spent.hashCode() : 0;
        result = 31 * result + (spentCurrency != null ? spentCurrency.hashCode() : 0);
        result = 31 * result + (earned != null ? earned.hashCode() : 0);
        result = 31 * result + (earnedCurrency != null ? earnedCurrency.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    public BigDecimal getSpent() {
        return spent;
    }

    public String getSpentCurrency() {
        return spentCurrency;
    }

    public BigDecimal getEarned() {
        return earned;
    }

    public String getEarnedCurrency() {
        return earnedCurrency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("spent", spent.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .add("spentCurrency", spentCurrency)
                .add("earned", earned.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString())
                .add("earnedCurrency", earnedCurrency)
                .add("timestamp", timestamp)
                .toString();
    }
}
