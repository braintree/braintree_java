package com.braintreegateway;

public class PaymentMethodOptionsAdyenRequest extends Request {
    private PaymentMethodOptionsRequest parent;
    private Boolean overwriteBrand;
    private String selectedBrand;

    public PaymentMethodOptionsAdyenRequest() {}

    public PaymentMethodOptionsAdyenRequest(PaymentMethodOptionsRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodOptionsRequest done() {
        return parent;
    }

    public PaymentMethodOptionsAdyenRequest overwriteBrand(Boolean overwriteBrand) {
        this.overwriteBrand = overwriteBrand;
        return this;
    }

    public PaymentMethodOptionsAdyenRequest selectedBrand(String selectedBrand) {
        this.selectedBrand = selectedBrand;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("adyen").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("adyen");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        builder.addElement("overwriteBrand", overwriteBrand);
        builder.addElement("selectedBrand", selectedBrand);

        return builder;
    }
}
