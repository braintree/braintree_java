package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalHereDetails {
    private String authorizationId;
    private String captureId;
    private String invoiceId;
    private String last4;
    private String paymentId;
    private String paymentType;
    private String refundId;
    private String transactionFeeAmount;
    private String transactionFeeCurrencyIsoCode;
    private String transactionInitiationDate;
    private String transactionUpdatedDate;

    public PayPalHereDetails(NodeWrapper node) {
        authorizationId = node.findString("authorization-id");
        captureId = node.findString("capture-id");
        invoiceId = node.findString("invoice-id");
        last4 = node.findString("last-4");
        paymentId = node.findString("payment-id");
        paymentType = node.findString("payment-type");
        refundId = node.findString("refund-id");
        transactionFeeAmount = node.findString("transaction-fee-amount");
        transactionFeeCurrencyIsoCode = node.findString("transaction-fee-currency-iso-code");
        transactionInitiationDate = node.findString("transaction-initiation-date");
        transactionUpdatedDate = node.findString("transaction-updated-date");
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getLast4() {
        return last4;
    }

    public String getTransactionInitiationDate() {
        return transactionInitiationDate;
    }

    public String getTransactionUpdatedDate() {
        return transactionUpdatedDate;
    }

    public String getTransactionFeeAmount() {
        return transactionFeeAmount;
    }

    public String getTransactionFeeCurrencyIsoCode() {
        return transactionFeeCurrencyIsoCode;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public String getCaptureId() {
        return captureId;
    }

    public String getRefundId() {
        return refundId;
    }
}
