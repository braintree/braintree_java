package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetails {
    private String cardType;
    private String lastTwo;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        lastTwo = node.findString("last-two");
    }

    public String getCardType() {
        return cardType;
    }

    public String getLastTwo() {
        return lastTwo;
    }

}
