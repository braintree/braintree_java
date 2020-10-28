package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalDetails {
    private String payerEmail;
    private String paymentId;
    private String authorizationId;
    private String token;
    private String imageUrl;
    private String debugId;
    private String payeeId;
    private String payeeEmail;
    private String customField;
    private String payerId;
    private String payerFirstName;
    private String payerLastName;
    private String payerStatus;
    private String sellerProtectionStatus;
    private String captureId;
    private String refundId;
    private String transactionFeeAmount;
    private String transactionFeeCurrencyIsoCode;
    private String refundFromTransactionFeeAmount;
    private String refundFromTransactionFeeCurrencyIsoCode;
    private String description;
    private String implicitlyVaultedPaymentMethodToken;
    private String implicitlyVaultedPaymentMethodGlobalId;
    private String billingAgreementId;

    public PayPalDetails(NodeWrapper node) {
        payerEmail = node.findString("payer-email");
        paymentId = node.findString("payment-id");
        authorizationId = node.findString("authorization-id");
        token = node.findString("token");
        imageUrl = node.findString("image-url");
        debugId = node.findString("debug-id");
        payeeId = node.findString("payee-id");
        payeeEmail = node.findString("payee-email");
        customField = node.findString("custom-field");
        payerId = node.findString("payer-id");
        payerFirstName = node.findString("payer-first-name");
        payerLastName = node.findString("payer-last-name");
        payerStatus = node.findString("payer-status");
        sellerProtectionStatus = node.findString("seller-protection-status");
        refundId = node.findString("refund-id");
        captureId = node.findString("capture-id");
        transactionFeeAmount = node.findString("transaction-fee-amount");
        transactionFeeCurrencyIsoCode = node.findString("transaction-fee-currency-iso-code");
        refundFromTransactionFeeAmount = node.findString("refund-from-transaction-fee-amount");
        refundFromTransactionFeeCurrencyIsoCode = node.findString("refund-from-transaction-fee-currency-iso-code");
        description = node.findString("description");
        implicitlyVaultedPaymentMethodToken = node.findString("implicitly-vaulted-payment-method-token");
        implicitlyVaultedPaymentMethodGlobalId = node.findString("implicitly-vaulted-payment-method-global-id");
        billingAgreementId = node.findString("billing-agreement-id");
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDebugId() {
        return debugId;
    }

    public String getPayeeId() {
      return payeeId;
    }

    public String getPayeeEmail() {
      return payeeEmail;
    }

    public String getCustomField() {
      return customField;
    }

    public String getPayerId() {
      return payerId;
    }

    public String getPayerFirstName() {
      return payerFirstName;
    }

    public String getPayerLastName() {
      return payerLastName;
    }

    public String getPayerStatus() {
      return payerStatus;
    }

    public String getSellerProtectionStatus() {
      return sellerProtectionStatus;
    }

    public String getCaptureId() {
      return captureId;
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

    public String getRefundFromTransactionFeeAmount() {
      return refundFromTransactionFeeAmount;
    }

    public String getRefundFromTransactionFeeCurrencyIsoCode() {
      return refundFromTransactionFeeCurrencyIsoCode;
    }

    public String getDescription() {
      return description;
    }

    public String getImplicitlyVaultedPaymentMethodToken() {
        return implicitlyVaultedPaymentMethodToken;
    }

    public String getImplicitlyVaultedPaymentMethodGlobalId() {
        return implicitlyVaultedPaymentMethodGlobalId;
    }

    public String getBillingAgreementId() {
        return billingAgreementId;
    }
}
