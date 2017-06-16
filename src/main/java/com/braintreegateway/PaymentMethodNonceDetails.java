package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetails {
    private String cardType;
    private String lastTwo;
    private String correlationId;
    private String email;
    private String payerInfo;
    private String username;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        cardType = node.findString("cardType");
        lastTwo = node.findString("lastTwo");
        correlationId = node.findString("correlationId");
        email = node.findString("email");
        payerInfo = node.findString("payerInfo");
        username = node.findString("username");
    }

    public String getCardType() {
        return cardType;
    }

    public String getLastTwo() {
        return lastTwo;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getEmail() {
        return email;
    }

    public String getPayerInfo() {
        return payerInfo;
    }

    public String getUsername() {
        return username;
    }
}
