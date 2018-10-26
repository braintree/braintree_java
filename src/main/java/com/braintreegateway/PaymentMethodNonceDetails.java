package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetails {
    private String cardType;
    private String lastTwo;
    private String lastFour;
    private String username;
    private String venmoUserId;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        lastTwo = node.findString("last-two");
        lastFour = node.findString("last-four");
        username = node.findString("username");
        venmoUserId = node.findString("venmo-user-id");
    }

    public String getCardType() {
        return cardType;
    }

    public String getLastTwo() {
        return lastTwo;
    }

    public String getLastFour() {
        return lastFour;
    }

    public String getUsername() {
        return username;
    }

    public String getVenmoUserId() {
        return venmoUserId;
    }
}
