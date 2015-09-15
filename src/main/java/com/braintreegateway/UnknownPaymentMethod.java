package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class UnknownPaymentMethod implements PaymentMethod {
    private String token;
    private String customerId;
    private boolean isDefault;

    public UnknownPaymentMethod(NodeWrapper node) {
        token = node.findString("token");
        customerId = node.findString("customer-id");
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

    public String getCustomerId() {
        return customerId;
    }
}
