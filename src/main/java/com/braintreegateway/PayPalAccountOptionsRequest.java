package com.braintreegateway;

public class PayPalAccountOptionsRequest extends Request {
    private Boolean makeDefault;
    private PayPalAccountRequest parent;

    public PayPalAccountOptionsRequest() {

    }

    public PayPalAccountOptionsRequest(PayPalAccountRequest parent) {
        this.parent = parent;
    }

    public PayPalAccountRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public PayPalAccountOptionsRequest makeDefault(Boolean makeDefault) {
        this.makeDefault = makeDefault;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).addElement("makeDefault", makeDefault);
    }
}
