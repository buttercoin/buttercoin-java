package com.buttercoin.api;

import java.math.BigDecimal;

class CreateDeposit {
    private String method;
    private String currency;
    private BigDecimal amount;

    public CreateDeposit(String method, String currency, BigDecimal amount) {
        this.method = method;
        this.currency = currency;
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
}
