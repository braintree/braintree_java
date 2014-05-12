package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalAccount implements PaymentMethod {
    private String email;
    private String token;

    public PayPalAccount(NodeWrapper node) {
        this.email = node.findString("email");
        this.token = node.findString("token");
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
