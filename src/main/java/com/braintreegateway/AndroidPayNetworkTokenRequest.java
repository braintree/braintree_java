package com.braintreegateway;

public class AndroidPayNetworkTokenRequest extends Request {
    private String cardholderName;
    private String cryptogram;
    private String customerId;
    private String eciIndicator;
    private String expirationMonth;
    private String expirationYear;
    private String googleTransactionId;
    private String number;
    private String token;
    private AndroidPayNetworkTokenAddressRequest billingAddressRequest;
    private AndroidPayNetworkTokenOptionsRequest optionsRequest;
    private CustomerRequest parent;

    public AndroidPayNetworkTokenRequest () {
    }

    public AndroidPayNetworkTokenRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public AndroidPayNetworkTokenAddressRequest billingAddress() {
        billingAddressRequest = new AndroidPayNetworkTokenAddressRequest(this);
        return billingAddressRequest;
    }

    public AndroidPayNetworkTokenOptionsRequest options() {
        this.optionsRequest = new AndroidPayNetworkTokenOptionsRequest(this);
        return optionsRequest;
    }

    public AndroidPayNetworkTokenRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public AndroidPayNetworkTokenRequest cryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public AndroidPayNetworkTokenRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerRequest done() {
        return parent;
    }
    
    public AndroidPayNetworkTokenRequest eciIndicator(String eciIndicator) {
        this.eciIndicator = eciIndicator;
        return this;
    }

    public AndroidPayNetworkTokenRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public AndroidPayNetworkTokenRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public AndroidPayNetworkTokenRequest googleTransactionId(String googleTransactionId) {
        this.googleTransactionId = googleTransactionId;
        return this;
    }

    public AndroidPayNetworkTokenRequest number(String number) {
        this.number = number;
        return this;
    }

    public AndroidPayNetworkTokenRequest token(String token) {
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
        return buildRequest("androidPayNetworkToken").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("androidPayNetworkToken");
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
