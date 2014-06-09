package com.braintreegateway;

public class PaymentMethodOptionsRequest extends Request {
    private Boolean makeDefault;
    private PaymentMethodRequest parent;

    public PaymentMethodOptionsRequest() {}

    public PaymentMethodOptionsRequest(PaymentMethodRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodRequest done() {
        return parent;
    }

    public Boolean getMakeDefault() {
        return makeDefault;
    }

    public PaymentMethodOptionsRequest makeDefault(Boolean makeDefault) {
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
