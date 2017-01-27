package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodOptionsPayPalRequest extends Request {
    private PaymentMethodOptionsRequest parent;
    private String payeeEmail;

    public PaymentMethodOptionsPayPalRequest(PaymentMethodOptionsRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodOptionsRequest done() {
        return parent;
    }

    public PaymentMethodOptionsPayPalRequest payeeEmail(String payeeEmail) {
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
