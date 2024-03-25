package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Map;

public class PaymentMethodNonceDetails {

    private PaymentMethodNonceDetailsPayerInfo payerInfo;
    private PaymentMethodNonceDetailsSepaDirectDebit sepaDirectDebit;
    private String bin;
    private String cardType;
    private String cardholderName;
    private String dpanLastTwo;
    private String email;
    private String expirationMonth;
    private String expirationYear;
    private Boolean isNetworkTokenized;
    private String lastFour;
    private String lastTwo;
    private String paymentInstrumentName;
    private String username;
    private String venmoUserId;

    public PaymentMethodNonceDetails(NodeWrapper node) {
        bin = node.findString("bin");
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        dpanLastTwo = node.findString("dpan-last-two");
        email = node.findString("email");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        isNetworkTokenized = node.findBoolean("is-network-tokenized");
        lastFour = node.findString("last-four");
        lastTwo = node.findString("last-two");
        paymentInstrumentName = node.findString("payment-instrument-name");
        username = node.findString("username");
        venmoUserId = node.findString("venmo-user-id");

        NodeWrapper payerInfoNode = node.findFirst("payer-info");
        if (payerInfoNode != null && !payerInfoNode.isBlank()) {
            payerInfo = new PaymentMethodNonceDetailsPayerInfo(payerInfoNode);
        }

        if (node.findString("bank-reference-token") != null && node.findString("iban-last-chars") != null) {
            sepaDirectDebit = new PaymentMethodNonceDetailsSepaDirectDebit(node);
        }
    }

    public PaymentMethodNonceDetails(Map<String, Object> map) {
        bin = (String) map.get("bin");
        cardType = (String) map.get("card-type");
        cardholderName = (String) map.get("cardholder-name");
        dpanLastTwo = (String) map.get("dpan-last-two");
        email = (String) map.get("email");
        expirationMonth = (String) map.get("expiration-month");
        expirationYear = (String) map.get("expiration-year");
        isNetworkTokenized = (Boolean)map.get("is-network-tokenized");
        lastFour = (String) map.get("last-four");
        lastTwo = (String) map.get("last-two");
        paymentInstrumentName = (String) map.get("payment-instrument-name");
        username = (String) map.get("username");
        venmoUserId = (String) map.get("venmo-user-id");

        Map<String, String> payerInfoMap = (Map) map.get("payer-info");
        if (payerInfoMap != null) {
            payerInfo = new PaymentMethodNonceDetailsPayerInfo(payerInfoMap);
        }

        if (map.get("bank-reference-token") != null && map.get("iban-last-chars") != null) {
            sepaDirectDebit = new PaymentMethodNonceDetailsSepaDirectDebit(map);
        }
    }

    public String getBin() {
        return bin;
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

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public Boolean isNetworkTokenized() {
        return isNetworkTokenized;
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

    public PaymentMethodNonceDetailsSepaDirectDebit getSepaDirectDebit() {
        return sepaDirectDebit;
    }
}
