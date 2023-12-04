package com.braintreegateway;

// NEXT_MAJOR_VERSION remove venmoSdkSession
// The old venmo SDK integration has been deprecated
public class CreditCardOptionsRequest extends Request {
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean makeDefault;
    private Boolean skipAdvancedFraudChecking;
    private Boolean verifyCard;
    private CreditCardRequest parent;
    private String updateExistingToken;
    @Deprecated
    private String venmoSdkSession;
    private String verificationAccountType; // NEXT_MAJOR_VERSION - This should be enum with [credit, debit]
    private String verificationAmount;
    private String verificationCurrencyIsoCode;
    private String verificationMerchantAccountId;

    public CreditCardOptionsRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardRequest done() {
        return parent;
    }

    public CreditCardOptionsRequest verificationMerchantAccountId(String verificationMerchantAccountId) {
        this.verificationMerchantAccountId = verificationMerchantAccountId;
        return this;
    }

    public CreditCardOptionsRequest verificationAccountType(String verificationAccountType) {
        this.verificationAccountType = verificationAccountType;
        return this;
    }

    public CreditCardOptionsRequest failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    public CreditCardOptionsRequest verificationAmount(String verificationAmount) {
        this.verificationAmount = verificationAmount;
        return this;
    }

    public CreditCardOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public CreditCardOptionsRequest skipAdvancedFraudChecking(Boolean skipAdvancedFraudChecking) {
        this.skipAdvancedFraudChecking = skipAdvancedFraudChecking;
        return this;
    }

    public CreditCardOptionsRequest makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }

    public CreditCardOptionsRequest updateExistingToken(String token) {
        this.updateExistingToken = token;
        return this;
    }

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
    */
    public CreditCardOptionsRequest venmoSdkSession(String venmoSdkSession) {
        this.venmoSdkSession = venmoSdkSession;
        return this;
    }

    public CreditCardOptionsRequest verificationCurrencyIsoCode(String verificationCurrencyIsoCode) {
        this.verificationCurrencyIsoCode = verificationCurrencyIsoCode;
        return this;
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
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("failOnDuplicatePaymentMethod", failOnDuplicatePaymentMethod);
        builder.addElement("skipAdvancedFraudChecking", skipAdvancedFraudChecking);
        builder.addElement("updateExistingToken", updateExistingToken);
        builder.addElement("venmoSdkSession", venmoSdkSession);
        builder.addElement("verificationAccountType", verificationAccountType);
        builder.addElement("verificationAmount", verificationAmount);
        builder.addElement("verificationCurrencyIsoCode", verificationCurrencyIsoCode);
        builder.addElement("verificationMerchantAccountId", verificationMerchantAccountId);
        builder.addElement("verifyCard", verifyCard);
        if (makeDefault != null && makeDefault.booleanValue()) {
            builder.addElement("makeDefault", makeDefault);
        }

        return builder;
    }
}
