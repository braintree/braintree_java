package com.braintreegateway;

public class SubscriptionOptionsRequest extends Request {
    private Boolean doNotInheritAddOnsOrDiscounts;
    private SubscriptionRequest parent;
    private Boolean prorateCharges;
    private Boolean replaceAllAddOnsAndDiscounts;
    private Boolean revertSubscriptionOnProrationFailure;
    private Boolean startImmediately;
    private SubscriptionOptionsPayPalRequest paypal;

    public SubscriptionOptionsRequest(SubscriptionRequest parent) {
        this.parent = parent;
    }

    public SubscriptionRequest done() {
        return parent;
    }

    public SubscriptionOptionsRequest doNotInheritAddOnsOrDiscounts(Boolean doNotInheritAddOnsOrDiscounts) {
        this.doNotInheritAddOnsOrDiscounts = doNotInheritAddOnsOrDiscounts;
        return this;
    }

    public SubscriptionOptionsRequest prorateCharges(Boolean prorateCharges) {
        this.prorateCharges = prorateCharges;
        return this;
    }

    public SubscriptionOptionsRequest replaceAllAddOnsAndDiscounts(Boolean replaceAllAddonsAndDiscounts) {
        this.replaceAllAddOnsAndDiscounts = replaceAllAddonsAndDiscounts;
        return this;
    }

    public SubscriptionOptionsRequest revertSubscriptionOnProrationFailure(Boolean revertSubscriptionOnProrationFailure) {
        this.revertSubscriptionOnProrationFailure = revertSubscriptionOnProrationFailure;
        return this;
    }

    public SubscriptionOptionsRequest startImmediately(Boolean startImmediately) {
        this.startImmediately = startImmediately;
        return this;
    }

    public SubscriptionOptionsPayPalRequest paypal() {
        this.paypal = new SubscriptionOptionsPayPalRequest(this);
        return this.paypal;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("doNotInheritAddOnsOrDiscounts", doNotInheritAddOnsOrDiscounts)
            .addElement("prorateCharges", prorateCharges)
            .addElement("replaceAllAddOnsAndDiscounts", replaceAllAddOnsAndDiscounts)
            .addElement("revertSubscriptionOnProrationFailure", revertSubscriptionOnProrationFailure)
            .addElement("startImmediately", startImmediately)
            .addElement("paypal", paypal);
    }
}
