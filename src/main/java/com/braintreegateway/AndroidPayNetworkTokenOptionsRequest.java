package com.braintreegateway;

public class AndroidPayNetworkTokenOptionsRequest extends Request {
    private Boolean makeDefault;
    private AndroidPayNetworkTokenRequest parent;

    public AndroidPayNetworkTokenOptionsRequest() {}

    public AndroidPayNetworkTokenOptionsRequest(AndroidPayNetworkTokenRequest parent) {
        this.parent = parent;
    }

    public AndroidPayNetworkTokenRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public AndroidPayNetworkTokenOptionsRequest makeDefault(Boolean makeDefault) {
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
