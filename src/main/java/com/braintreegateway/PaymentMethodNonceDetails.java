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
    private PaymentMethodNonceDetailsPayerInfo payerInfo;

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

        NodeWrapper payerInfoNode = node.findFirst("payer-info");
        if (payerInfoNode != null && !payerInfoNode.isBlank()) {
            payerInfo = new PaymentMethodNonceDetailsPayerInfo(payerInfoNode);
        }
    }

    public PaymentMethodNonceDetails(Map<String, Object> map) {
        cardType = (String) map.get("card-type");
        cardholderName = (String) map.get("cardholder-name");
        dpanLastTwo = (String) map.get("dpan-last-two");
        email = (String) map.get("email");
        bin = (String) map.get("bin");
        lastFour = (String) map.get("last-four");
        lastTwo = (String) map.get("last-two");
        paymentInstrumentName = (String) map.get("payment-instrument-name");
        username = (String) map.get("username");
        venmoUserId = (String) map.get("venmo-user-id");

        Map<String, String> payerInfoMap = (Map) map.get("payer-info");
        if (payerInfoMap != null) {
            payerInfo = new PaymentMethodNonceDetailsPayerInfo(payerInfoMap);
        }
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

    public PaymentMethodNonceDetailsPayerInfo getPayerInfo() {
        return payerInfo;
    }
}
