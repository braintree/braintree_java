package com.braintreegateway;

public class PaymentMethodRequest extends Request {
    private String paymentMethodNonce;
    private String customerId;
    private String token;
    private PaymentMethodOptionsRequest optionsRequest;
    private PaymentMethodAddressRequest billingAddressRequest;
    private String billingAddressId;
    private String deviceData;
    private String cardholderName;
    private String cvv;
    private String deviceSessionId;
    private String fraudMerchantId;
    private String expirationDate;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private String paymentMethodToken;
    private String paypalRefreshToken;
    private String venmoSdkPaymentMethodCode;
    private PaymentMethodThreeDSecurePassThruRequest threeDSecurePassThruRequest;

    public PaymentMethodRequest() {
    }

    public PaymentMethodRequest paymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
        return this;
    }

    public PaymentMethodRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public PaymentMethodRequest cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public PaymentMethodRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
    }

    @Deprecated
    // Merchants should be using deviceData only
    public PaymentMethodRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }

    public PaymentMethodRequest expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public PaymentMethodRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public PaymentMethodRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public PaymentMethodRequest number(String number) {
        this.number = number;
        return this;
    }

    public PaymentMethodRequest paypalRefreshToken(String paypalRefreshToken) {
        this.paypalRefreshToken = paypalRefreshToken;
        return this;
    }

    public PaymentMethodRequest venmoSdkPaymentMethodCode(String venmoSdkPaymentMethodCode) {
        this.venmoSdkPaymentMethodCode = venmoSdkPaymentMethodCode;
        return this;
    }

    public PaymentMethodOptionsRequest options() {
        this.optionsRequest = new PaymentMethodOptionsRequest(this);
        return optionsRequest;
    }

    public String getToken() {
        return token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public PaymentMethodRequest token(String token) {
        this.token = token;
        return this;
    }

    public PaymentMethodRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public PaymentMethodRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    public PaymentMethodRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public PaymentMethodAddressRequest billingAddress() {
        billingAddressRequest = new PaymentMethodAddressRequest(this);
        return billingAddressRequest;
    }

    public PaymentMethodRequest billingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
        return this;
    }

    public PaymentMethodThreeDSecurePassThruRequest threeDSecurePassThruRequest() {
        this.threeDSecurePassThruRequest = new PaymentMethodThreeDSecurePassThruRequest(this);
        return threeDSecurePassThruRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("payment-method").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("customer-id", customerId)
            .addElement("token", token)
            .addElement("options", optionsRequest)
            .addElement("threeDSecurePassThru", threeDSecurePassThruRequest)
            .addElement("payment-method-nonce", paymentMethodNonce)
            .addElement("billingAddress", billingAddressRequest)
            .addElement("billingAddressId", billingAddressId)
            .addElement("deviceData", deviceData)
            .addElement("customerId", customerId)
            .addElement("cardholderName", cardholderName)
            .addElement("cvv", cvv)
            .addElement("number", number)
            .addElement("deviceSessionId", deviceSessionId)
            .addElement("fraudMerchantId", fraudMerchantId)
            .addElement("expirationDate", expirationDate)
            .addElement("expirationMonth", expirationMonth)
            .addElement("expirationYear", expirationYear)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("paypalRefreshToken", paypalRefreshToken)
            .addElement("venmoSdkPaymentMethodCode", venmoSdkPaymentMethodCode);

        return builder;
    }
}
