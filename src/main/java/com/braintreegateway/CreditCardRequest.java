package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCard CreditCards}.
 */
public class CreditCardRequest extends Request {
    private CreditCardAddressRequest billingAddressRequest;
    private String billingAddressId;
    private String deviceData;
    private String cardholderName;
    private String customerId;
    private String cvv;
    private String deviceSessionId;
    private String fraudMerchantId;
    private String expirationDate;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private CreditCardOptionsRequest optionsRequest;
    private CustomerRequest parent;
    private String token;
    private String paymentMethodToken;
    private String paymentMethodNonce;
    private String venmoSdkPaymentMethodCode;
    private CreditCardThreeDSecurePassThruRequest threeDSecurePassThruRequest;

    public CreditCardRequest() {
    }

    public CreditCardRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public CreditCardAddressRequest billingAddress() {
        billingAddressRequest = new CreditCardAddressRequest(this);
        return billingAddressRequest;
    }

    public CreditCardRequest billingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
        return this;
    }

    public CreditCardRequest deviceData(String deviceData) {
        this.deviceData = deviceData;
        return this;
    }

    public CreditCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public CreditCardRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreditCardRequest cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public CreditCardRequest deviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
        return this;
    }

    public CreditCardRequest fraudMerchantId(String fraudMerchantId) {
        this.fraudMerchantId = fraudMerchantId;
        return this;
    }
    public CustomerRequest done() {
        return parent;
    }

    public CreditCardRequest expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public CreditCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public CreditCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public CreditCardRequest paymentMethodNonce(String nonce) {
        this.paymentMethodNonce = nonce;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getToken() {
        return token;
    }

    public CreditCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public CreditCardRequest venmoSdkPaymentMethodCode(String venmoSdkPaymentMethodCode) {
        this.venmoSdkPaymentMethodCode = venmoSdkPaymentMethodCode;
        return this;
    }

    public CreditCardOptionsRequest options() {
        this.optionsRequest = new CreditCardOptionsRequest(this);
        return optionsRequest;
    }

    public CreditCardThreeDSecurePassThruRequest threeDSecurePassThruRequest() {
        this.threeDSecurePassThruRequest = new CreditCardThreeDSecurePassThruRequest(this);
        return threeDSecurePassThruRequest;
    }

    public CreditCardRequest paymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
        return this;
    }

    public CreditCardRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("creditCard").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("creditCard");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).
            addTopLevelElement("paymentMethodToken", paymentMethodToken).
            toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("billingAddress", billingAddressRequest).
            addElement("billingAddressId", billingAddressId).
            addElement("deviceData", deviceData).
            addElement("options", optionsRequest).
            addElement("threeDSecurePassThru", threeDSecurePassThruRequest).
            addElement("customerId", customerId).
            addElement("cardholderName", cardholderName).
            addElement("cvv", cvv).
            addElement("number", number).
            addElement("deviceSessionId", deviceSessionId).
            addElement("fraudMerchantId", fraudMerchantId).
            addElement("expirationDate", expirationDate).
            addElement("expirationMonth", expirationMonth).
            addElement("expirationYear", expirationYear).
            addElement("paymentMethodNonce", paymentMethodNonce).
            addElement("token", token).
            addElement("venmoSdkPaymentMethodCode", venmoSdkPaymentMethodCode);
    }
}
