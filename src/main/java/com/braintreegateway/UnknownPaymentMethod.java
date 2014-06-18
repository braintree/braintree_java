package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class UnknownPaymentMethod implements PaymentMethod {
    private String token;
    private boolean isDefault;

    public UnknownPaymentMethod(NodeWrapper node) {
        token = node.findString("token");
        isDefault = node.findBoolean("default");
    }

    public String getToken() {
        return token;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getImageUrl() {
        return "https://assets.braintreegateway.com/payment_method_logo/unknown.png";
    }
}
