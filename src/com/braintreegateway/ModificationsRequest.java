package com.braintreegateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModificationsRequest extends Request {
    private SubscriptionRequest parent;
    private String name;
    private List<ModificationRequest> updates;
    private List<String> removeModificationIds;

    public ModificationsRequest(SubscriptionRequest parent, String name) {
        this.name = name;
        this.parent = parent;
        this.updates = new ArrayList<ModificationRequest>();
        this.removeModificationIds = new ArrayList<String>();
    }

    public SubscriptionRequest done() {
        return parent;
    }
    
    public ModificationsRequest remove(String... modificationIds) {
        return remove(Arrays.asList(modificationIds));
    }
    
    public ModificationsRequest remove(List<String> modificationIds) {
        removeModificationIds.addAll(modificationIds);
        return this;
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
            addElement("remove", removeModificationIds).
            addElement("update", updates);
    }
}
