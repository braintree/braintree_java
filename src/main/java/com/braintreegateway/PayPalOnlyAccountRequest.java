package com.braintreegateway;

public class PayPalOnlyAccountRequest extends Request {
    private String clientId;
    private String clientSecret;
    private MerchantRequest parent;

    public PayPalOnlyAccountRequest() {

    }

    public PayPalOnlyAccountRequest(MerchantRequest parent) {
        this.parent = parent;
    }

    public MerchantRequest done() {
        return parent;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public PayPalOnlyAccountRequest clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public PayPalOnlyAccountRequest clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    @Override
    public String toXML() {
        return new RequestBuilder("paypalAccount").
            addElement("clientId", clientId).
            addElement("clientSecret", clientSecret).
            toXML();
    }
}
