package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class TransactionOptionsAdyenRequest extends Request {
    private TransactionOptionsRequest parent;
    private Boolean overwriteBrand;
    private String selectedBrand;

    public TransactionOptionsAdyenRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsAdyenRequest overwriteBrand(Boolean overwriteBrand) {
        this.overwriteBrand = overwriteBrand;
        return this;
    }

    public TransactionOptionsAdyenRequest selectedBrand(String selectedBrand) {
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
