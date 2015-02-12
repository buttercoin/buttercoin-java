package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class Order extends SimpleOrder {
    public static enum Side {
        Buy("buy"), Sell("sell");

        private final String name;

        Side(String name) {
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

    public static enum OrderType {
        Market("market"), Limit("limit");

        private final String name;

        OrderType(String name) {
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

    public static enum Status {
        Opened("opened"), PartialFilled("partial-filled"), Filled("filled"), Canceled("canceled");

        private final String name;

        Status(String name) {
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

    public static class Event {
        public static enum EventType {
            Created("created"), Opened("opened"), Filled("filled"), PartialFilled("partial-filled"), Canceled("canceled");

            private final String name;

            EventType(String name) {
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

        private EventType eventType;
        private String eventDate;
        private BigDecimal offered;
        private BigDecimal received;
        private BigDecimal refund;
        private BigDecimal quantity;

        public EventType getEventType() {
            return eventType;
        }

        public String getEventDate() {
            return eventDate;
        }

        public BigDecimal getOffered() {
            return offered;
        }

        public BigDecimal getReceived() {
            return received;
        }

        public BigDecimal getRefund() {
            return refund;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
            helper.add("eventType", eventType);
            helper.add("eventDate", eventDate);
            if (offered != null) helper.add("offered", offered.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            if (received != null) helper.add("received", received.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            if (refund != null) helper.add("refund", refund.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            if (quantity != null) helper.add("quantity", quantity.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            return helper.toString();
        }
    }

    private String orderId;
    private Side side;
    private OrderType orderType;
    private Status status;
    private String priceCurrency;
    private String quantityCurrency;
    private List<Event> events;

    public String getOrderId() {
        return orderId;
    }

    public Side getSide() {
        return side;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Status getStatus() {
        return status;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getQuantityCurrency() {
        return quantityCurrency;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);

        if (price != null) {
            if ("BTC".equalsIgnoreCase(priceCurrency)) helper.add("price", price.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            else helper.add("price", price.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
        }
        helper.add("priceCurrency", priceCurrency);

        if (quantity != null) {
            if ("BTC".equalsIgnoreCase(quantityCurrency)) helper.add("quantity", quantity.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            else helper.add("quantity", quantity.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }

        helper.add("quantityCurrency", quantityCurrency);

        helper.add("orderId", orderId);
        helper.add("side", side);
        helper.add("orderType", orderType);
        helper.add("status", status);
        helper.add("events", events);
        return helper.toString();
    }
}
