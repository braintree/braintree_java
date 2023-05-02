package com.braintreegateway;

public class AndroidPayCardOptionsRequest extends Request {
    private Boolean makeDefault;
    private AndroidPayCardRequest parent;

    public AndroidPayCardOptionsRequest() {}

    public AndroidPayCardOptionsRequest(AndroidPayCardRequest parent) {
        this.parent = parent;
    }

    public AndroidPayCardRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public AndroidPayCardOptionsRequest makeDefault(Boolean makeDefault) {
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