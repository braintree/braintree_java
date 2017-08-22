package com.braintreegateway;

public class SubscriptionTransactionOptionsRequest extends Request {
    private Boolean submitForSettlement;
    private SubscriptionTransactionRequest parent;

    public SubscriptionTransactionOptionsRequest(SubscriptionTransactionRequest parent) {
        this.parent = parent;
    }

    public SubscriptionTransactionRequest done() {
        return parent;
    }

    public SubscriptionTransactionOptionsRequest submitForSettlement(Boolean submitForSettlement) {
        this.submitForSettlement = submitForSettlement;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("options").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("submitForSettlement", submitForSettlement);
    }
}
