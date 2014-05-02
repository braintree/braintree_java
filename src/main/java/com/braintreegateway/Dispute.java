package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public final class Dispute {
    public enum Status {
        OPEN,
        LOST,
        WON;
    }

    public enum Reason {
        CANCELLED_RECURRING_TRANSACTION,
        CREDIT_NOT_PROCESSED,
        DUPLICATE,
        FRAUD,
        GENERAL,
        INVALID_ACCOUNT,
        NOT_RECOGNIZED,
        PRODUCT_NOT_RECEIVED,
        PRODUCT_UNSATISFACTORY,
        TRANSACTION_AMOUNT_DIFFERS;
    }

    private final Calendar receivedDate;
    private final Calendar replyByDate;
    private final String currencyIsoCode;
    private final String id;
    private final Reason reason;
    private final Status status;
    private final BigDecimal amount;

    public Dispute(NodeWrapper node) {
        receivedDate = node.findDate("received-date");
        replyByDate = node.findDate("reply-by-date");
        currencyIsoCode = node.findString("currency-iso-code");
        reason = EnumUtils.findByName(Reason.class, node.findString("reason"));
        status = EnumUtils.findByName(Status.class, node.findString("status"));
        amount = node.findBigDecimal("amount");
        id = node.findString("id");
    }

    public Calendar getReceivedDate() {
        return receivedDate;
    }

    public Calendar getReplyByDate() {
        return replyByDate;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public String getId() {
        return id;
    }

    public Reason getReason() {
        return reason;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
