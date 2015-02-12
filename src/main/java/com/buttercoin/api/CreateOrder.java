package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonValue;

// todo move Instrument somewhere else?
public class CreateOrder {
    public enum Instrument {
        BTC_USD("BTC_USD"), USD_BTC("USD_BTC");

        private final String name;

        Instrument(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }
    }

    private Instrument instrument;
    private Order.Side side;
    private Order.OrderType orderType;
    private String price;
    private String quantity;

    public CreateOrder(Instrument instrument, Order.Side side, Order.OrderType orderType, String price, String quantity) {
        this.instrument = instrument;
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.quantity = quantity;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Order.Side getSide() {
        return side;
    }

    public Order.OrderType getOrderType() {
        return orderType;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public void setSide(Order.Side side) {
        this.side = side;
    }

    public void setOrderType(Order.OrderType orderType) {
        this.orderType = orderType;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
