package com.braintreegateway;

public class SubscriptionOptionsPayPalRequest extends Request {
    private SubscriptionOptionsRequest parent;
    private String description;

    public SubscriptionOptionsPayPalRequest(SubscriptionOptionsRequest parent) {
        this.parent = parent;
    }

    public SubscriptionOptionsRequest done() {
        return parent;
    }

    public SubscriptionOptionsPayPalRequest description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paypal").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("description", description);
    }
}
