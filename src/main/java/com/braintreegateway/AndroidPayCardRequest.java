package com.braintreegateway;

public class AndroidPayCardRequest extends Request {
    private String cardholderName;
    private String cryptogram;
    private String customerId;
    private String eciIndicator;
    private String expirationMonth;
    private String expirationYear;
    private String googleTransactionId;
    private String number;
    private String token;
    private AndroidPayCardAddressRequest billingAddressRequest;
    private AndroidPayCardOptionsRequest optionsRequest;
    private CustomerRequest parent;

    public AndroidPayCardRequest() {
    }

    public AndroidPayCardRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public AndroidPayCardAddressRequest billingAddress() {
        billingAddressRequest = new AndroidPayCardAddressRequest(this);
        return billingAddressRequest;
    }

    public AndroidPayCardOptionsRequest options() {
        this.optionsRequest = new AndroidPayCardOptionsRequest(this);
        return optionsRequest;
    }

    public AndroidPayCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public AndroidPayCardRequest cryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public AndroidPayCardRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerRequest done() {
        return parent;
    }

    public AndroidPayCardRequest eciIndicator(String eciIndicator) {
        this.eciIndicator = eciIndicator;
        return this;
    }

    public AndroidPayCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public AndroidPayCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public AndroidPayCardRequest googleTransactionId(String googleTransactionId) {
        this.googleTransactionId = googleTransactionId;
        return this;
    }

    public AndroidPayCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public AndroidPayCardRequest token(String token) {
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
        return buildRequest("androidPayCard").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("androidPayCard");
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
            .addElement("googleTransactionId", googleTransactionId)
            .addElement("number", number)
            .addElement("options", optionsRequest)
            .addElement("token", token);
    }
}
