package com.braintreegateway;

public class CreditCardOptionsRequest extends Request {
    private CreditCardRequest parent;
    private String verificationMerchantAccountId;
    private String verificationAccountType;
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean verifyCard;
    private String verificationAmount;
    private Boolean makeDefault;
    private String updateExistingToken;
    private String venmoSdkSession;
    private String verificationCurrencyIsoCode;

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

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("failOnDuplicatePaymentMethod", failOnDuplicatePaymentMethod);
        builder.addElement("verifyCard", verifyCard);
        builder.addElement("verificationAmount", verificationAmount);
        builder.addElement("verificationMerchantAccountId", verificationMerchantAccountId);
        builder.addElement("verificationAccountType", verificationAccountType);
        if (makeDefault != null && makeDefault.booleanValue()) {
            builder.addElement("makeDefault", makeDefault);
        }
        builder.addElement("updateExistingToken", updateExistingToken);
        builder.addElement("venmoSdkSession", venmoSdkSession);
        builder.addElement("verificationCurrencyIsoCode", verificationCurrencyIsoCode);

        return builder;
    }
}
