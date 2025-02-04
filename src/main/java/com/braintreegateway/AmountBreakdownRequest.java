package com.braintreegateway;

import java.math.BigDecimal;

public class AmountBreakdownRequest extends Request {
    private BigDecimal discount;
    private BigDecimal handling;
    private BigDecimal insurance;
    private BigDecimal itemTotal;
    private BigDecimal shipping;
    private BigDecimal shippingDiscount;
    private BigDecimal taxTotal;

    private PayPalPaymentResourceRequest parent;

    public AmountBreakdownRequest(PayPalPaymentResourceRequest parent) {
        this.parent = parent;
    }

    public AmountBreakdownRequest() {
    }

    public AmountBreakdownRequest discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public AmountBreakdownRequest handling(BigDecimal handling) {
        this.handling = handling;
        return this;
    }

    public AmountBreakdownRequest insurance(BigDecimal insurance) {
        this.insurance = insurance;
        return this;
    }

    public AmountBreakdownRequest itemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
        return this;
    }

    public AmountBreakdownRequest shipping(BigDecimal shipping) {
        this.shipping = shipping;
        return this;
    }

    public AmountBreakdownRequest shippingDiscount(BigDecimal shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
        return this;
    }

    public AmountBreakdownRequest taxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
        return this;
    }

    public PayPalPaymentResourceRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("amountBreakdown").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("amountBreakdown");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("discount", discount)
            .addElement("handling", handling)
            .addElement("insurance", insurance)
            .addElement("itemTotal", itemTotal)
            .addElement("shipping", shipping)
            .addElement("shippingDiscount", shippingDiscount)
            .addElement("taxTotal", taxTotal);
    }
}