package com.braintreegateway;

public class CreditCardOptionsRequest extends Request {
    private CreditCardRequest parent;
    private String verificationMerchantAccountId;
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean verifyCard;
    private Boolean makeDefault;
    private String updateExistingToken;
    private String venmoSdkSession;

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

    public CreditCardOptionsRequest failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    public CreditCardOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
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

    public CreditCardOptionsRequest venmoSdkSession(String venmoSdkSession) {
        this.venmoSdkSession = venmoSdkSession;
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

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("failOnDuplicatePaymentMethod", failOnDuplicatePaymentMethod);
        builder.addElement("verifyCard", verifyCard);
        builder.addElement("verificationMerchantAccountId", verificationMerchantAccountId);
        if (makeDefault != null && makeDefault.booleanValue()) {
            builder.addElement("makeDefault", makeDefault);
        }
        builder.addElement("updateExistingToken", updateExistingToken);
        builder.addElement("venmoSdkSession", venmoSdkSession);

        return builder;
    }
}
