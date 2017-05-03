package com.braintreegateway;

public class TransactionOptionsRequest extends Request {
    private Boolean addBillingAddressToPaymentMethod;
    private Boolean holdInEscrow;
    private TransactionRequest parent;
    private Boolean storeInVault;
    private Boolean storeInVaultOnSuccess;
    private Boolean storeShippingAddressInVault;
    private Boolean submitForSettlement;
    private String venmoSdkSession;
    private String payeeEmail;
    private Boolean skipAdvancedFraudChecking;
    private Boolean skipAvs;
    private Boolean skipCvv;
    private TransactionOptionsPayPalRequest transactionOptionsPayPalRequest;
    private TransactionOptionsAmexRewardsRequest transactionOptionsAmexRewardsRequest;
    private TransactionOptionsThreeDSecureRequest transactionOptionsThreeDSecureRequest;

    public TransactionOptionsRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest addBillingAddressToPaymentMethod(Boolean addBillingAddressToPaymentMethod) {
        this.addBillingAddressToPaymentMethod = addBillingAddressToPaymentMethod;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public TransactionOptionsRequest holdInEscrow(Boolean holdInEscrow) {
        this.holdInEscrow = holdInEscrow;
        return this;
    }

    public TransactionOptionsRequest storeInVault(Boolean storeInVault) {
        this.storeInVault = storeInVault;
        return this;
    }

    public TransactionOptionsRequest storeInVaultOnSuccess(Boolean storeInVaultOnSuccess) {
        this.storeInVaultOnSuccess = storeInVaultOnSuccess;
        return this;
    }

    public TransactionOptionsRequest storeShippingAddressInVault(Boolean storeShippingAddressInVault) {
        this.storeShippingAddressInVault = storeShippingAddressInVault;
        return this;
    }

    public TransactionOptionsRequest submitForSettlement(Boolean submitForSettlement) {
        this.submitForSettlement = submitForSettlement;
        return this;
    }

    public TransactionOptionsRequest venmoSdkSession(String venmoSdkSession) {
        this.venmoSdkSession = venmoSdkSession;
        return this;
    }

    public TransactionOptionsRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public TransactionOptionsRequest skipAdvancedFraudChecking(Boolean skipAdvancedFraudChecking) {
        this.skipAdvancedFraudChecking = skipAdvancedFraudChecking;
        return this;
    }

    public TransactionOptionsRequest skipAvs(Boolean skipAvs) {
        this.skipAvs = skipAvs;
        return this;
    }

    public TransactionOptionsRequest skipCvv(Boolean skipCvv) {
        this.skipCvv = skipCvv;
        return this;
    }

    public TransactionOptionsPayPalRequest paypal() {
        transactionOptionsPayPalRequest = new TransactionOptionsPayPalRequest(this);
        return transactionOptionsPayPalRequest;
    }

    public TransactionOptionsAmexRewardsRequest amexRewards() {
        transactionOptionsAmexRewardsRequest = new TransactionOptionsAmexRewardsRequest(this);
        return transactionOptionsAmexRewardsRequest;
    }

    public TransactionOptionsThreeDSecureRequest threeDSecure() {
        transactionOptionsThreeDSecureRequest = new TransactionOptionsThreeDSecureRequest(this);
        return transactionOptionsThreeDSecureRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("options");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("holdInEscrow", holdInEscrow).
            addElement("storeInVault", storeInVault).
            addElement("storeInVaultOnSuccess", storeInVaultOnSuccess).
            addElement("addBillingAddressToPaymentMethod", addBillingAddressToPaymentMethod).
            addElement("storeShippingAddressInVault", storeShippingAddressInVault).
            addElement("submitForSettlement", submitForSettlement).
            addElement("venmoSdkSession", venmoSdkSession).
            addElement("payeeEmail", payeeEmail).
            addElement("skipAdvancedFraudChecking", skipAdvancedFraudChecking).
            addElement("skipAvs", skipAvs).
            addElement("skipCvv", skipCvv).
            addElement("threeDSecure", transactionOptionsThreeDSecureRequest).
            addElement("paypal", transactionOptionsPayPalRequest).
            addElement("payWithAmexRewards", transactionOptionsAmexRewardsRequest);
    }
}
