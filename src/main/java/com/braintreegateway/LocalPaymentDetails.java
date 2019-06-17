package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentDetails {
    private String captureId;
    private String customField;
    private String debugId;
    private String description;
    private String fundingSource;
    private String payerId;
    private String paymentId;
    private String refundFromTransactionFeeAmount;
    private String refundFromTransactionFeeCurrencyIsoCode;
    private String refundId;
    private String transactionFeeAmount;
    private String transactionFeeCurrencyIsoCode;

    public LocalPaymentDetails(NodeWrapper node) {
        captureId = node.findString("capture-id");
        customField = node.findString("custom-field");
        debugId = node.findString("debug-id");
        description = node.findString("description");
        fundingSource = node.findString("funding-source");
        payerId = node.findString("payer-id");
        paymentId = node.findString("payment-id");
        refundFromTransactionFeeAmount = node.findString("refund-from-transaction-fee-amount");
        refundFromTransactionFeeCurrencyIsoCode = node.findString("refund-from-transaction-fee-currency-iso-code");
        refundId = node.findString("refund-id");
        transactionFeeAmount = node.findString("transaction-fee-amount");
        transactionFeeCurrencyIsoCode = node.findString("transaction-fee-currency-iso-code");
    }

    public String getCaptureId() {
      return captureId;
    }

    public String getCustomField() {
      return customField;
    }

    public String getDebugId() {
      return debugId;
    }

    public String getDescription() {
      return description;
    }

    public String getFundingSource() {
      return fundingSource;
    }

    public String getPayerId() {
      return payerId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getRefundFromTransactionFeeAmount() {
      return refundFromTransactionFeeAmount;
    }

    public String getRefundFromTransactionFeeCurrencyIsoCode() {
      return refundFromTransactionFeeCurrencyIsoCode;
    }

    public String getRefundId() {
      return refundId;
    }

    public String getTransactionFeeAmount() {
      return transactionFeeAmount;
    }

    public String getTransactionFeeCurrencyIsoCode() {
      return transactionFeeCurrencyIsoCode;
    }

}
