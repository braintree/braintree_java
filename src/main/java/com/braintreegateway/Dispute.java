package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class Dispute {
    public enum Status {
        OPEN,
        LOST,
        WON,
        UNRECOGNIZED,
        ACCEPTED,
        DISPUTED,
        EXPIRED
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

    private final Calendar createdAt;
    private final Calendar receivedDate;
    private final Calendar replyByDate;
    private final Calendar openedDate;
    private final Calendar updatedAt;
    private final Calendar wonDate;
    private final List<DisputeEvidence> evidence;
    private final List<DisputeStatusHistory> statusHistory;
    private final String caseNumber;
    private final String currencyIsoCode;
    private final String id;
    private final String forwardedComments;
    private final String merchantAccountId;
    private final String originalDisputeId;
    private final String reasonCode;
    private final String reasonDescription;
    private final String referenceNumber;
    private final Reason reason;
    private final Status status;
    private final Kind kind;
    private final BigDecimal amount;
    private final BigDecimal disputedAmount;
    private final BigDecimal wonAmount;
    private final TransactionDetails transactionDetails;
    private final DisputeTransaction transaction;

    public Dispute(NodeWrapper node) {
        createdAt = node.findDateTime("created-at");
        receivedDate = node.findDate("received-date");
        replyByDate = node.findDate("reply-by-date");
        openedDate = node.findDate("date-opened");
        updatedAt = node.findDateTime("updated-at");
        wonDate = node.findDate("date-won");

        caseNumber = node.findString("case-number");
        currencyIsoCode = node.findString("currency-iso-code");
        forwardedComments = node.findString("forwarded-comments");
        merchantAccountId = node.findString("merchant-account-id");
        originalDisputeId = node.findString("original-dispute-id");
        reasonCode = node.findString("reason-code");
        reasonDescription = node.findString("reason-description");
        referenceNumber = node.findString("reference-number");

        reason = EnumUtils.findByName(Reason.class, node.findString("reason"), Reason.GENERAL);
        status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        amount = node.findBigDecimal("amount");
        disputedAmount = node.findBigDecimal("amount-disputed");
        wonAmount = node.findBigDecimal("amount-won");
        id = node.findString("id");
        transaction = new DisputeTransaction(node.findFirst("transaction"));
        transactionDetails = new TransactionDetails(node.findFirst("transaction"));

        evidence = new ArrayList<DisputeEvidence>();
        for (NodeWrapper evidenceNode : node.findAll("evidence/evidence")) {
            evidence.add(new DisputeEvidence(evidenceNode));
        }

        statusHistory = new ArrayList<DisputeStatusHistory>();
        for (NodeWrapper statusNode : node.findAll("status-history/status-history")) {
            statusHistory.add(new DisputeStatusHistory(statusNode));
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
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

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public Calendar getWonDate() {
        return wonDate;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public String getForwardedComments() {
        return forwardedComments;
    }

    public String getId() {
        return id;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public String getOriginalDisputeId() {
        return originalDisputeId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public String getReferenceNumber() {
        return referenceNumber;
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

    public BigDecimal getDisputedAmount() {
        return disputedAmount;
    }

    public BigDecimal getWonAmount() {
        return wonAmount;
    }

    public List<DisputeEvidence> getEvidence() {
        return evidence;
    }

    public List<DisputeStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    /**
     * Please use Transaction.getTransaction() instead
     */
    @Deprecated
    public TransactionDetails getTransactionDetails() {
      return transactionDetails;
    }

    public DisputeTransaction getTransaction() {
        return transaction;
    }
}
