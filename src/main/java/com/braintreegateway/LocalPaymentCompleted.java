package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.List;
import java.util.ArrayList;

public class LocalPaymentCompleted {
    private String paymentId;
    private String payerId;

    public LocalPaymentCompleted(NodeWrapper node) {
        this.paymentId = node.findString("payment-id");
        this.payerId = node.findString("payer-id");
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getPayerId() {
        return payerId;
    }
 }
