package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalDetails {
    private String payerEmail;
    private String payerFirstName;
    private String payerLastName;
    private String payerId;
    private String paymentId;
    private String saleId;
    private String token;

    public PayPalDetails(NodeWrapper node) {
        payerEmail = node.findString("payer-email");
        payerFirstName = node.findString("payer-first-name");
        payerLastName = node.findString("payer-last-name");
        paymentId = node.findString("payment-id");
        saleId = node.findString("sale-id");
        token = node.findString("token");
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getSaleId() {
        return saleId;
    }

    public String getToken() {
        return token;
    }
}
