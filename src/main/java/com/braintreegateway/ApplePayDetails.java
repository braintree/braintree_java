package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class ApplePayDetails {
    private String cardType;
    private String cardholderName;
    private String expirationMonth;
    private String expirationYear;
    private String token;

    public ApplePayDetails(NodeWrapper node) {
        token = node.findString("token");
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
    }

    public String getToken() {
        return token;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }
}
