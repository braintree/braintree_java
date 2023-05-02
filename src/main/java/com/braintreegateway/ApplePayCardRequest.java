package com.braintreegateway;

public class ApplePayCardRequest extends Request {
    private String cardholderName;
    private String cryptogram;
    private String customerId;
    private String eciIndicator;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private String token;
    private ApplePayCardAddressRequest billingAddressRequest;
    private ApplePayCardOptionsRequest optionsRequest;
    private CustomerRequest parent;

    public ApplePayCardRequest() {
    }

    public ApplePayCardRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public ApplePayCardAddressRequest billingAddress() {
        billingAddressRequest = new ApplePayCardAddressRequest(this);
        return billingAddressRequest;
    }

    public ApplePayCardOptionsRequest options() {
        this.optionsRequest = new ApplePayCardOptionsRequest(this);
        return optionsRequest;
    }

    public ApplePayCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public ApplePayCardRequest cryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public ApplePayCardRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerRequest done() {
        return parent;
    }

    public ApplePayCardRequest eciIndicator(String eciIndicator) {
        this.eciIndicator = eciIndicator;
        return this;
    }

    public ApplePayCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public ApplePayCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public ApplePayCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public ApplePayCardRequest token(String token) {
        this.token = token;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toXML() {
        return buildRequest("applePayCard").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("applePayCard");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("billingAddress", billingAddressRequest)
            .addElement("cryptogram", cryptogram)
            .addElement("customerId", customerId)
            .addElement("cardholderName", cardholderName)
            .addElement("eciIndicator", eciIndicator)
            .addElement("expirationMonth", expirationMonth)
            .addElement("expirationYear", expirationYear)
            .addElement("number", number)
            .addElement("options", optionsRequest)
            .addElement("token", token);
    }
}
