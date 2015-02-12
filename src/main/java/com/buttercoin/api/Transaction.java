package com.buttercoin.api;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.util.List;

public class Transaction {
    public static enum Status {
        Pending("pending"), Processing("processing"), Funded("funded"), Canceled("canceled"), Failed("failed");

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

    public static enum TransactionType {
        Deposit("deposit"), Withdrawal("withdrawal");

        private final String name;

        TransactionType(String name) {
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
            Created("created"), Pending("pending"), Processing("processing"), Suspended("suspended"), Funded("funded"), Canceled("canceled"), Failed("failed");

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

        public EventType getEventType() {
            return eventType;
        }

        public String getEventDate() {
            return eventDate;
        }
    }

    public static class FundingInfo {
        private String source;
        private String reason;
        private String bank;
        private String bankName;
        private String externalId;
        private String bitcoinTransactionId;
        private String destinationAddress;

        public String getSource() {
            return source;
        }

        public String getReason() {
            return reason;
        }

        public String getBank() {
            return bank;
        }

        public String getBankName() {
            return bankName;
        }

        public String getExternalId() {
            return externalId;
        }

        public String getBitcoinTransactionId() {
            return bitcoinTransactionId;
        }

        public String getDestinationAddress() {
            return destinationAddress;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("source", source)
                    .add("reason", reason)
                    .add("bank", bank)
                    .add("bankName", bankName)
                    .add("externalId", externalId)
                    .add("bitcoinTransactionId", bitcoinTransactionId)
                    .add("destinationAddress", destinationAddress)
                    .toString();
        }
    }

    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal fees;
    private String currency;
    private BigDecimal amount;
    private String method;
    private Status status;
    private FundingInfo fundingInfo;
    private List<Event> events;

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public Status getStatus() {
        return status;
    }

    public FundingInfo getFundingInfo() {
        return fundingInfo;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        helper.add("transactionId", transactionId);
        helper.add("transactionType", transactionType);

        if ("BTC".equalsIgnoreCase(currency)) {
            helper.add("fees", fees.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
            helper.add("amount", amount.setScale(8, BigDecimal.ROUND_HALF_DOWN).toString());
        }
        else {
            helper.add("fees", fees.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
            helper.add("amount", amount.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
        }

        helper.add("currency", currency);
        helper.add("method", method);
        helper.add("status", status);
        helper.add("fundingInfo", fundingInfo);
        helper.add("events", events);
        return helper.toString();
    }
}
