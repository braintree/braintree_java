package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonceDetails {
    private String cardType;
    private String cardholderName;
    private String dpanLastTwo;
    private String email;
    private String bin;
    private String lastFour;
    private String lastTwo;
    private String paymentInstrumentName;
    private String username;
    private String venmoUserId;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        dpanLastTwo = node.findString("dpan-last-two");
        email = node.findString("email");
        bin = node.findString("bin");
        lastFour = node.findString("last-four");
        lastTwo = node.findString("last-two");
        paymentInstrumentName = node.findString("payment-instrument-name");
        username = node.findString("username");
        venmoUserId = node.findString("venmo-user-id");
    }

    public PaymentMethodNonceDetails(Map<String, String> map) {
        cardType = map.get("card-type");
        cardholderName = map.get("cardholder-name");
        dpanLastTwo = map.get("dpan-last-two");
        email = map.get("email");
        bin = map.get("bin");
        lastFour = map.get("last-four");
        lastTwo = map.get("last-two");
        paymentInstrumentName = map.get("payment-instrument-name");
        username = map.get("username");
        venmoUserId = map.get("venmo-user-id");
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getDpanLastTwo() {
        return dpanLastTwo;
    }

    public String getEmail() {
        return email;
    }

    public String getBin() {
        return bin;
    }

    public String getLastTwo() {
        return lastTwo;
    }

    public String getLastFour() {
        return lastFour;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public String getUsername() {
        return username;
    }

    public String getVenmoUserId() {
        return venmoUserId;
    }
}
