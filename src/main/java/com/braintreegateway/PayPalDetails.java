package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalDetails {
    private String payerEmail;
    private String payerFirstName;
    private String payerLastName;
    private String payerId;
    private String paymentId;
    private String saleId;

    public PayPalDetails(NodeWrapper node) {
        payerEmail = node.findString("payer-email");
        payerFirstName = node.findString("payer-first-name");
        payerLastName = node.findString("payer-last-name");
        paymentId = node.findString("payment-id");
        saleId = node.findString("sale-id");
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
}
