package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PayPalDetails {
    private String authorizationId;
    private String billingAgreementId;
    private String captureId;
    private String customField;
    private String debugId;
    private String description;
    private String imageUrl;
    private String implicitlyVaultedPaymentMethodGlobalId;
    private String implicitlyVaultedPaymentMethodToken;
    private String payeeEmail;
    private String payeeId;
    private String payerEmail;
    private String payerId;
    private String paymentId;
    private String payerFirstName;
    private String payerLastName;
    private String payerStatus;
    private String recipientEmail; 
    private RecipientPhone recipientPhone; 
    private String refundFromTransactionFeeAmount;
    private String refundFromTransactionFeeCurrencyIsoCode;
    private String refundId;
    private String sellerProtectionStatus;
    private String taxId;
    private String taxIdType;
    private String token;
    private String transactionFeeAmount;
    private String transactionFeeCurrencyIsoCode;

    public PayPalDetails(NodeWrapper node) {
        authorizationId = node.findString("authorization-id");
        billingAgreementId = node.findString("billing-agreement-id");
        captureId = node.findString("capture-id");
        customField = node.findString("custom-field");
        debugId = node.findString("debug-id");
        description = node.findString("description");
        imageUrl = node.findString("image-url");
        implicitlyVaultedPaymentMethodGlobalId = node.findString("implicitly-vaulted-payment-method-global-id");
        implicitlyVaultedPaymentMethodToken = node.findString("implicitly-vaulted-payment-method-token");
        payeeEmail = node.findString("payee-email");
        payeeId = node.findString("payee-id");
        payerEmail = node.findString("payer-email");
        payerId = node.findString("payer-id");
        paymentId = node.findString("payment-id");
        payerFirstName = node.findString("payer-first-name");
        payerLastName = node.findString("payer-last-name");
        payerStatus = node.findString("payer-status");
        recipientEmail = node.findString("recipient-email"); 
        NodeWrapper recipientPhoneNode = node.findFirst("recipient-phone");
        if (recipientPhoneNode != null) {
            recipientPhone = new RecipientPhone(recipientPhoneNode);
        }
        refundFromTransactionFeeAmount = node.findString("refund-from-transaction-fee-amount");
        refundFromTransactionFeeCurrencyIsoCode = node.findString("refund-from-transaction-fee-currency-iso-code");
        refundId = node.findString("refund-id");
        sellerProtectionStatus = node.findString("seller-protection-status");
        taxId = node.findString("tax-id");
        taxIdType = node.findString("tax-id-type");
        token = node.findString("token");
        transactionFeeAmount = node.findString("transaction-fee-amount");
        transactionFeeCurrencyIsoCode = node.findString("transaction-fee-currency-iso-code");
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public String getBillingAgreementId() {
        return billingAgreementId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImplicitlyVaultedPaymentMethodGlobalId() {
        return implicitlyVaultedPaymentMethodGlobalId;
    }

    public String getImplicitlyVaultedPaymentMethodToken() {
        return implicitlyVaultedPaymentMethodToken;
    }

    public String getPayeeEmail() {
      return payeeEmail;
    }

    public String getPayeeId() {
      return payeeId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getPayerId() {
      return payerId;
    }

    public String getPaymentId() {
        return paymentId;
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

    public String getRecipientEmail(){
      return recipientEmail; 
    }

    public RecipientPhone getRecipientPhone(){
      return recipientPhone; 
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

    public String getSellerProtectionStatus() {
      return sellerProtectionStatus;
    }

    public String getTaxId() {
      return taxId;
    }

    public String getTaxIdType() {
      return taxIdType;
    }

    public String getToken() {
      return token;
    }

    public String getTransactionFeeAmount() {
      return transactionFeeAmount;
    }

    public String getTransactionFeeCurrencyIsoCode() {
      return transactionFeeCurrencyIsoCode;
    }

}
