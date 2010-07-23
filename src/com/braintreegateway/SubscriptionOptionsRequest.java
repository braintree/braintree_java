package com.braintreegateway;

public class SubscriptionOptionsRequest extends Request {
    private Boolean doNotInheritAddOnsOrDiscounts;
    private SubscriptionRequest parent;

    public SubscriptionOptionsRequest(SubscriptionRequest parent) {
        this.parent = parent;
    }

    public SubscriptionRequest done() {
        return parent;
    }

    public SubscriptionOptionsRequest doNotInheritAddOnsOrDiscounts(boolean doNotInheritAddOnsOrDiscounts) {
        this.doNotInheritAddOnsOrDiscounts = doNotInheritAddOnsOrDiscounts;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("doNotInheritAddOnsOrDiscounts", doNotInheritAddOnsOrDiscounts);
    }
}
