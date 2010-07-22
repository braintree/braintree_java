package com.braintreegateway;

public class ModificationsRequest extends Request {
    private ModificationRequest modificationRequest;
    private SubscriptionRequest parent;
    private String name;

    public ModificationsRequest(SubscriptionRequest parent, String name) {
        this.name = name;
        this.parent = parent;
    }

    public SubscriptionRequest done() {
        return parent;
    }

    public ModificationRequest update() {
        modificationRequest = new ModificationRequest(this);
        return modificationRequest;
    }

    @Override
    public String toXML() {
        return buildRequest(name).toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("update", modificationRequest);
    }
}
