package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public final class Dispute {
    public enum Status {
        OPEN,
        LOST,
        WON,
        UNRECOGNIZED;
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
        TRANSACTION_AMOUNT_DIFFERS,
        RETRIEVAL;
    }

    public enum Kind {
        CHARGEBACK,
        PRE_ARBITRATION,
        RETRIEVAL,
        UNRECOGNIZED;
    }

    private final Calendar receivedDate;
    private final Calendar replyByDate;
    private final Calendar openedDate;
    private final Calendar wonDate;
    private final String currencyIsoCode;
    private final String id;
    private final Reason reason;
    private final Status status;
    private final Kind kind;
    private final BigDecimal amount;
    private final TransactionDetails transactionDetails;

    public Dispute(NodeWrapper node) {
        receivedDate = node.findDate("received-date");
        replyByDate = node.findDate("reply-by-date");
        openedDate = node.findDate("date-opened");
        wonDate = node.findDate("date-won");
        currencyIsoCode = node.findString("currency-iso-code");
        reason = EnumUtils.findByName(Reason.class, node.findString("reason"), Reason.GENERAL);
        status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        amount = node.findBigDecimal("amount");
        id = node.findString("id");
        transactionDetails = new TransactionDetails(node.findFirst("transaction"));
    }

    public Calendar getReceivedDate() {
        return receivedDate;
    }

    public Calendar getReplyByDate() {
        return replyByDate;
    }

    public Calendar getOpenedDate() {
        return openedDate;
    }

    public Calendar getWonDate() {
        return wonDate;
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

    public Kind getKind() {
        return kind;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionDetails getTransactionDetails() {
      return transactionDetails;
    }
}
