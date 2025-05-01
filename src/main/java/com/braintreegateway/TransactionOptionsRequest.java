package com.braintreegateway;

// NEXT_MAJOR_VERSION remove venmoSdkSession
// The old venmo SDK integration has been deprecated
public class TransactionOptionsRequest extends Request {
    private Boolean addBillingAddressToPaymentMethod;
    private TransactionRequest parent;
    private Boolean storeInVault;
    private Boolean storeInVaultOnSuccess;
    private Boolean storeShippingAddressInVault;
    private Boolean submitForSettlement;
    private String venmoSdkSession;
    private String payeeId;
    private String payeeEmail;
    private Boolean skipAdvancedFraudChecking;
    private Boolean skipAvs;
    private Boolean skipCvv;
    private TransactionOptionsPayPalRequest transactionOptionsPayPalRequest;
    private TransactionOptionsAdyenRequest transactionOptionsAdyenRequest;
    private TransactionOptionsAmexRewardsRequest transactionOptionsAmexRewardsRequest;
    private TransactionOptionsThreeDSecureRequest transactionOptionsThreeDSecureRequest;
    private TransactionOptionsVenmoRequest transactionOptionsVenmoRequest;
    private TransactionOptionsCreditCardRequest transactionOptionsCreditCardRequest;
    private TransactionOptionsProcessingOverridesRequest transactionOptionsProcessingOverridesRequest;

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

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
    */
    @Deprecated
    public TransactionOptionsRequest venmoSdkSession(String venmoSdkSession) {
        this.venmoSdkSession = venmoSdkSession;
        return this;
    }

    public TransactionOptionsRequest payeeId(String payeeId) {
        this.payeeId = payeeId;
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

    public TransactionOptionsAdyenRequest adyen() {
        transactionOptionsAdyenRequest = new TransactionOptionsAdyenRequest(this);
        return transactionOptionsAdyenRequest;
    }

    public TransactionOptionsAmexRewardsRequest amexRewards() {
        transactionOptionsAmexRewardsRequest = new TransactionOptionsAmexRewardsRequest(this);
        return transactionOptionsAmexRewardsRequest;
    }

    public TransactionOptionsThreeDSecureRequest threeDSecure() {
        transactionOptionsThreeDSecureRequest = new TransactionOptionsThreeDSecureRequest(this);
        return transactionOptionsThreeDSecureRequest;
    }

    public TransactionOptionsVenmoRequest venmo() {
        transactionOptionsVenmoRequest = new TransactionOptionsVenmoRequest(this);
        return transactionOptionsVenmoRequest;
    }

    public TransactionOptionsCreditCardRequest creditCard() {
        transactionOptionsCreditCardRequest = new TransactionOptionsCreditCardRequest(this);
        return transactionOptionsCreditCardRequest;
    }

    public TransactionOptionsProcessingOverridesRequest processingOverrides() {
        transactionOptionsProcessingOverridesRequest = new TransactionOptionsProcessingOverridesRequest(this);
        return transactionOptionsProcessingOverridesRequest;
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

    // NEXT_MAJOR_VERSION remove venmoSdkSession
    // The old venmo SDK integration has been deprecated
    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("storeInVault", storeInVault)
            .addElement("storeInVaultOnSuccess", storeInVaultOnSuccess)
            .addElement("addBillingAddressToPaymentMethod", addBillingAddressToPaymentMethod)
            .addElement("storeShippingAddressInVault", storeShippingAddressInVault)
            .addElement("submitForSettlement", submitForSettlement)
            .addElement("venmoSdkSession", venmoSdkSession)
            .addElement("payeeId", payeeId)
            .addElement("payeeEmail", payeeEmail)
            .addElement("skipAdvancedFraudChecking", skipAdvancedFraudChecking)
            .addElement("skipAvs", skipAvs)
            .addElement("skipCvv", skipCvv)
            .addElement("threeDSecure", transactionOptionsThreeDSecureRequest)
            .addElement("venmo", transactionOptionsVenmoRequest)
            .addElement("adyen", transactionOptionsAdyenRequest)
            .addElement("paypal", transactionOptionsPayPalRequest)
            .addElement("payWithAmexRewards", transactionOptionsAmexRewardsRequest)
            .addElement("creditCard", transactionOptionsCreditCardRequest)
            .addElement("processingOverrides", transactionOptionsProcessingOverridesRequest);
    }
}
