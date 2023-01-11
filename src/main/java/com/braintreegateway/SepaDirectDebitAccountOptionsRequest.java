package com.braintreegateway;

public class SepaDirectDebitAccountOptionsRequest extends Request {
    private Boolean makeDefault;
    private SepaDirectDebitAccountRequest parent;

    public SepaDirectDebitAccountOptionsRequest() {}

    public SepaDirectDebitAccountOptionsRequest(SepaDirectDebitAccountRequest parent) {
        this.parent = parent;
    }

    public SepaDirectDebitAccountRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public SepaDirectDebitAccountOptionsRequest makeDefault(Boolean makeDefault) {
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
