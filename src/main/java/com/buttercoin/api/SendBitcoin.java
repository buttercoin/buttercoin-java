package com.buttercoin.api;

import java.math.BigDecimal;

class SendBitcoin {
    private String currency;
    private BigDecimal amount;
    private String destination;

    public SendBitcoin(String currency, BigDecimal amount, String destination) {
        this.currency = currency;
        this.amount = amount;
        this.destination = destination;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
