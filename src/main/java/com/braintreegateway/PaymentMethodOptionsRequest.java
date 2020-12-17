package com.braintreegateway;

public class PaymentMethodOptionsRequest extends Request {
    private Boolean makeDefault;
    private PaymentMethodRequest parent;
    private String verificationMerchantAccountId;
    private String verificationAccountType;
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean verifyCard;
    private String verificationAmount;
    private String venmoSdkSession;
    private String verificationCurrencyIsoCode;
    private PaymentMethodOptionsAdyenRequest paymentMethodOptionsAdyenRequest;
    private PaymentMethodOptionsPayPalRequest paymentMethodOptionsPayPalRequest;
    private UsBankAccountVerification.VerificationMethod usBankAccountVerificationMethod;

    public PaymentMethodOptionsRequest() {

    }

    public PaymentMethodOptionsRequest(PaymentMethodRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public PaymentMethodOptionsRequest makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }

    public PaymentMethodOptionsRequest verificationMerchantAccountId(String verificationMerchantAccountId) {
        this.verificationMerchantAccountId = verificationMerchantAccountId;
        return this;
    }

    public PaymentMethodOptionsRequest verificationAccountType(String verificationAccountType) {
        this.verificationAccountType = verificationAccountType;
        return this;
    }

    public PaymentMethodOptionsRequest failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    public PaymentMethodOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public PaymentMethodOptionsRequest verificationAmount(String verificationAmount) {
        this.verificationAmount = verificationAmount;
        return this;
    }

    public PaymentMethodOptionsRequest venmoSdkSession(String venmoSdkSession) {
        this.venmoSdkSession = venmoSdkSession;
        return this;
    }

    public PaymentMethodOptionsRequest verificationCurrencyIsoCode(String verificationCurrencyIsoCode) {
        this.verificationCurrencyIsoCode = verificationCurrencyIsoCode;
        return this;
    }

    public PaymentMethodOptionsPayPalRequest paypal() {
        paymentMethodOptionsPayPalRequest = new PaymentMethodOptionsPayPalRequest(this);
        return paymentMethodOptionsPayPalRequest;
    }

    public PaymentMethodOptionsAdyenRequest adyen() {
        paymentMethodOptionsAdyenRequest = new PaymentMethodOptionsAdyenRequest(this);
        return paymentMethodOptionsAdyenRequest;
    }

    public PaymentMethodOptionsRequest usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod verificationMethod) {
        this.usBankAccountVerificationMethod = verificationMethod;
        return this;
    }


    @Override
    public String toXML() {
        return buildRequest("options").toXML();
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

        builder.addElement("venmoSdkSession", venmoSdkSession);
        builder.addElement("verificationCurrencyIsoCode", verificationCurrencyIsoCode);
        builder.addElement("paypal", paymentMethodOptionsPayPalRequest);
        builder.addElement("adyen", paymentMethodOptionsAdyenRequest);
        builder.addElement("usBankAccountVerificationMethod", usBankAccountVerificationMethod);
        return builder;
    }
}
