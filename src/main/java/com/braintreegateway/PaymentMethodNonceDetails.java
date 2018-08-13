package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetails {
    private String cardType;
    private String lastTwo;
    private String lastFour;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        lastTwo = node.findString("last-two");
        lastFour = node.findString("last-four");
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
}
