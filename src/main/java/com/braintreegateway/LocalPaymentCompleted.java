package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.Transaction;
import java.util.List;
import java.util.ArrayList;

public class LocalPaymentCompleted {
    private String paymentId;
    private String payerId;
    private String paymentMethodNonce;
    private Transaction transaction;

    public LocalPaymentCompleted(NodeWrapper node) {
        this.paymentId = node.findString("payment-id");
        this.payerId = node.findString("payer-id");
        this.paymentMethodNonce = node.findString("payment-method-nonce");

        NodeWrapper transactionNode = node.findFirst("transaction");
        if (transactionNode != null) {
            this.transaction = new Transaction(transactionNode);
        }
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPaymentMethodNonce() {
        return paymentMethodNonce;
    }

    public Transaction getTransaction() {
        return transaction;
    }
 }
