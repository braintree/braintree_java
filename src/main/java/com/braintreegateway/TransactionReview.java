package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public final class TransactionReview {
    private final String transactionId;
    private final String decision;
    private final String reviewerEmail;
    private final String reviewerNote;
    private final Calendar reviewedTime;

    public TransactionReview(NodeWrapper node) {
        this.transactionId = node.findString("transaction-id");
        this.decision = node.findString("decision");
        this.reviewerEmail = node.findString("reviewer-email");
        this.reviewerNote = node.findString("reviewer-note");
        this.reviewedTime = node.findDateTime("reviewed-time");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDecision() {
        return decision;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public String getReviewerNote() {
        return reviewerNote;
    }

    public Calendar getReviewedTime() {
        return reviewedTime;
    }

}
