package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalDetails {
    private String payerEmail;
    private String paymentId;
    private String authorizationId;
    private String token;
    private String imageUrl;

    public PayPalDetails(NodeWrapper node) {
        payerEmail = node.findString("payer-email");
        paymentId = node.findString("payment-id");
        authorizationId = node.findString("authorization-id");
        token = node.findString("token");
        imageUrl = node.findString("image-url");
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
