package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

public class ModificationsRequest extends Request {
    private SubscriptionRequest parent;
    private String name;
    private List<ModificationRequest> updates;

    public ModificationsRequest(SubscriptionRequest parent, String name) {
        this.name = name;
        this.parent = parent;
        this.updates = new ArrayList<ModificationRequest>();
    }

    public SubscriptionRequest done() {
        return parent;
    }

    public ModificationRequest update() {
        ModificationRequest modificationRequest = new ModificationRequest(this);
        updates.add(modificationRequest);
        return modificationRequest;
    }

    @Override
    public String toXML() {
        return buildRequest(name).toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("update", updates);
    }
}
