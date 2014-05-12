package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class UnknownPaymentMethod implements PaymentMethod {
    private String token;

    public UnknownPaymentMethod(NodeWrapper node) {
        token = node.findString("token");
    }

    public String getToken() {
        return token;
    }
}
