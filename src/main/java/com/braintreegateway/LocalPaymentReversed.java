package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentReversed {
    private String paymentId;

    public LocalPaymentReversed(NodeWrapper node) {
        this.paymentId = node.findString("payment-id");
    }

    public String getPaymentId() {
        return paymentId;
    }
 }
