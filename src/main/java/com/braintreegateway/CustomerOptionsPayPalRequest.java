package com.braintreegateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomerOptionsPayPalRequest extends Request {
    private CustomerOptionsRequest parent;
    private CustomerOptionsPayPalShippingRequest shipping;
    private String payeeEmail;
    private String customField;
    private String description;
    private String orderId;
    private BigDecimal amount;

    public CustomerOptionsPayPalRequest(CustomerOptionsRequest parent) {
        this.parent = parent;
    }

    public CustomerOptionsRequest done() {
        return parent;
    }

    public CustomerOptionsPayPalShippingRequest shipping() {
        this.shipping = new CustomerOptionsPayPalShippingRequest(this);
        return shipping;
    }

    public CustomerOptionsPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public CustomerOptionsPayPalRequest customField(String customField) {
        this.customField = customField;
        return this;
    }

    public CustomerOptionsPayPalRequest description(String description) {
        this.description = description;
        return this;
    }

    public CustomerOptionsPayPalRequest orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public CustomerOptionsPayPalRequest amount(BigDecimal amount) {
        this.amount = amount;
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
            addElement("orderId", orderId).
            addElement("amount", amount).
            addElement("shipping", shipping);
    }
}
