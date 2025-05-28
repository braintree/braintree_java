package com.braintreegateway;

// NEXT_MAJOR_VERSION remove venmoSdkSession
// The old venmo SDK integration has been deprecated
public class PaymentMethodOptionsRequest extends Request {
    private String accountInformationInquiry;
    private Boolean failOnDuplicatePaymentMethod;
    private Boolean failOnDuplicatePaymentMethodForCustomer;
    private Boolean makeDefault;
    private Boolean skipAdvancedFraudChecking;
    private Boolean verifyCard;
    private PaymentMethodOptionsAdyenRequest paymentMethodOptionsAdyenRequest;
    private PaymentMethodOptionsPayPalRequest paymentMethodOptionsPayPalRequest;
    private PaymentMethodRequest parent;
    private String venmoSdkSession;
    private String verificationAccountType; // NEXT_MAJOR_VERSION - This should be enum with [credit, debit]
    private String verificationAmount;
    private String verificationCurrencyIsoCode;
    private String verificationMerchantAccountId;
    private UsBankAccountVerification.VerificationAddOns verificationAddOns;
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

    public PaymentMethodOptionsRequest accountInformationInquiry(String accountInformationInquiry) {
        this.accountInformationInquiry = accountInformationInquiry;
        return this;
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

    public PaymentMethodOptionsRequest verificationAddOns(UsBankAccountVerification.VerificationAddOns addOn) {
        this.verificationAddOns = addOn;
        return this;
    }

    public PaymentMethodOptionsRequest failOnDuplicatePaymentMethod(Boolean failOnDuplicatePaymentMethod) {
        this.failOnDuplicatePaymentMethod = failOnDuplicatePaymentMethod;
        return this;
    }

    public PaymentMethodOptionsRequest failOnDuplicatePaymentMethodForCustomer(Boolean failOnDuplicatePaymentMethodForCustomer) {
        this.failOnDuplicatePaymentMethodForCustomer = failOnDuplicatePaymentMethodForCustomer;
        return this;
    }

    public PaymentMethodOptionsRequest verifyCard(Boolean verifyCard) {
        this.verifyCard = verifyCard;
        return this;
    }

    public PaymentMethodOptionsRequest skipAdvancedFraudChecking(Boolean skipAdvancedFraudChecking) {
        this.skipAdvancedFraudChecking = skipAdvancedFraudChecking;
        return this;
    }

    public PaymentMethodOptionsRequest verificationAmount(String verificationAmount) {
        this.verificationAmount = verificationAmount;
        return this;
    }

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
    */
    @Deprecated
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

    // NEXT_MAJOR_VERSION remove venmoSdkSession
    // The old venmo SDK integration has been deprecated
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("accountInformationInquiry", accountInformationInquiry);
        builder.addElement("adyen", paymentMethodOptionsAdyenRequest);
        builder.addElement("failOnDuplicatePaymentMethod", failOnDuplicatePaymentMethod);
        builder.addElement("failOnDuplicatePaymentMethodForCustomer", failOnDuplicatePaymentMethodForCustomer);
        builder.addElement("paypal", paymentMethodOptionsPayPalRequest);
        builder.addElement("skipAdvancedFraudChecking", skipAdvancedFraudChecking);
        builder.addElement("usBankAccountVerificationMethod", usBankAccountVerificationMethod);
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
