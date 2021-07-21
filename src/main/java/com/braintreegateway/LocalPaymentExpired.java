package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentExpired {
    private String paymentId;
    private String paymentContextId;

    public LocalPaymentExpired(NodeWrapper node) {
        this.paymentId = node.findString("payment-id");
        this.paymentContextId = node.findString("payment-context-id");
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentContextId() {
        return paymentContextId;
    }
 }
