package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class LocalPaymentDetails {
    private String customField;
    private String description;
    private String fundingSource;
    private String payerId;
    private String paymentId;

    public LocalPaymentDetails(NodeWrapper node) {
        customField = node.findString("custom-field");
        description = node.findString("description");
        fundingSource = node.findString("funding-source");
        payerId = node.findString("payer-id");
        paymentId = node.findString("payment-id");
    }

    public String getCustomField() {
      return customField;
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

}
