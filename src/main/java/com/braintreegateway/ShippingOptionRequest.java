package com.braintreegateway;

import java.math.BigDecimal;

public class ShippingOptionRequest extends Request {

    private BigDecimal amount;
    private String id;
    private String label;
    private Boolean selected;
    private String type;

    private PayPalPaymentResourceRequest parent;

    public ShippingOptionRequest(PayPalPaymentResourceRequest parent) {
        this.parent = parent;
    }

    public ShippingOptionRequest() {
    }

    public ShippingOptionRequest amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ShippingOptionRequest id(String id) {
        this.id = id;
        return this;
    }

    public ShippingOptionRequest label(String label) {
        this.label = label;
        return this;
    }

    public ShippingOptionRequest selected(Boolean selected) {
        this.selected = selected;
        return this;
    }

    public ShippingOptionRequest type(String type) {
        this.type = type;
        return this;
    }

    public PayPalPaymentResourceRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("shippingOption").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("amount", amount)
            .addElement("id", id)
            .addElement("label", label)
            .addElement("selected", selected)
            .addElement("type", type);
    }
}