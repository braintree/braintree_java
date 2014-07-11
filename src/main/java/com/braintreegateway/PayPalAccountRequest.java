package com.braintreegateway;

public class PayPalAccountRequest extends Request {
    private String token;
    private PayPalAccountOptionsRequest optionsRequest;

    public PayPalAccountRequest token(String token) {
        this.token = token;
        return this;
    }

    public PayPalAccountOptionsRequest options() {
        this.optionsRequest = new PayPalAccountOptionsRequest(this);
        return optionsRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("paypalAccount").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("options", optionsRequest).
            addElement("token", token);
    }
}
