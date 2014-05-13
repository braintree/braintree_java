package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalAccount implements PaymentMethod {
    private String email;
    private String token;
    private boolean isDefault;

    public PayPalAccount(NodeWrapper node) {
        this.email = node.findString("email");
        this.token = node.findString("token");
        this.isDefault = node.findBoolean("default");
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public boolean isDefault() {
        return isDefault;
    }

}
