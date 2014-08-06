package com.braintreegateway;

public class PaymentMethodAddressOptionsRequest extends Request {

    private PaymentMethodAddressRequest parent;
    private Boolean updateExisting;

    public PaymentMethodAddressOptionsRequest(PaymentMethodAddressRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodAddressRequest done() {
        return parent;
    }

    public PaymentMethodAddressOptionsRequest updateExisting(Boolean updateExisting) {
        this.updateExisting = updateExisting;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString("options");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("updateExisting", updateExisting);
    }
}
