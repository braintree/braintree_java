package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentFunded {
    private String paymentId;
    private String paymentContextId;
    private Transaction transaction;

    public LocalPaymentFunded(NodeWrapper node) {
        this.paymentId = node.findString("payment-id");
        this.paymentContextId = node.findString("payment-context-id");

        NodeWrapper transactionNode = node.findFirst("transaction");
        this.transaction = new Transaction(transactionNode);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getPaymentContextId() {
        return paymentContextId;
    }
 }
