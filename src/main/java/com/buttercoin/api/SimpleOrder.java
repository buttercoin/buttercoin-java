package com.buttercoin.api;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class SimpleOrder {
    protected BigDecimal price;
    protected BigDecimal quantity;

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        // we're assuming price USD, quantity BTC - this is our current default and won't change in v1

        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        if (price != null) helper.add("price", price.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
        if (quantity != null) helper.add("quantity", quantity.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
        return helper.toString();
    }
}