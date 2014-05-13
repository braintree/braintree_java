package com.braintreegateway;

public class PayPalAccountRequest extends Request {
    private String token;

    public PayPalAccountRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paypalAccount").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("token", token);
    }
}
