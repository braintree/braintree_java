package com.braintreegateway;

public class ApplePayCardOptionsRequest extends Request {
    private Boolean makeDefault;
    private ApplePayCardRequest parent;

    public ApplePayCardOptionsRequest() {}

    public ApplePayCardOptionsRequest(ApplePayCardRequest parent) {
        this.parent = parent;
    }

    public ApplePayCardRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public ApplePayCardOptionsRequest makeDefault(Boolean makeDefault) {
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