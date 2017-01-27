package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class CustomerOptionsPayPalRequest extends Request {
    private CustomerOptionsRequest parent;
    private String payeeEmail;

    public CustomerOptionsPayPalRequest(CustomerOptionsRequest parent) {
        this.parent = parent;
    }

    public CustomerOptionsRequest done() {
        return parent;
    }

    public CustomerOptionsPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paypal").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paypal");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("payeeEmail", payeeEmail);
    }
}
