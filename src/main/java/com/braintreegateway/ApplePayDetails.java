package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class ApplePayDetails {
    private String cardType;
    private String paymentInstrumentName;
    private String sourceDescription;
    private String cardholderName;
    private String expirationMonth;
    private String expirationYear;
    private String last4;
    private String token;

    public ApplePayDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        paymentInstrumentName = node.findString("payment-instrument-name");
        sourceDescription = node.findString("source-description");
        cardholderName = node.findString("cardholder-name");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        last4 = node.findString("last-4");
        token = node.findString("token");
    }

    public String getToken() {
        return token;
    }

    public String getCardType() {
        return cardType;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public String getSourceDescription() {
        return sourceDescription;
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

    public String getLast4() {
        return last4;
    }
}
