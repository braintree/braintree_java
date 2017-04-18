package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodOptionsPayPalRequest extends Request {
    private PaymentMethodOptionsRequest parent;
    private String payeeEmail;
    private String customField;
    private String description;
    private String orderId;

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

    public PaymentMethodOptionsPayPalRequest description(String description) {
        this.description = description;
        return this;
    }
    public PaymentMethodOptionsPayPalRequest customField(String customField) {
        this.customField = customField;
        return this;
    }
    public PaymentMethodOptionsPayPalRequest orderId(String orderId) {
        this.orderId = orderId;
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
            addElement("payeeEmail", payeeEmail).
            addElement("description", description).
            addElement("customField", customField).
            addElement("orderId", orderId);
    }
}
