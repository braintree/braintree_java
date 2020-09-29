package com.braintreegateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModificationsRequest extends Request {
    private SubscriptionRequest parent;
    private String name;
    private List<AddModificationRequest> adds;
    private List<UpdateModificationRequest> updates;
    private List<String> removeModificationIds;

    public ModificationsRequest(SubscriptionRequest parent, String name) {
        this.name = name;
        this.parent = parent;
        this.adds = new ArrayList<AddModificationRequest>();
        this.updates = new ArrayList<UpdateModificationRequest>();
        this.removeModificationIds = new ArrayList<String>();
    }

    public AddModificationRequest add() {
        AddModificationRequest addModificationRequest = new AddModificationRequest(this);
        adds.add(addModificationRequest);
        return addModificationRequest;
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

    public UpdateModificationRequest update() {
        UpdateModificationRequest updateModificationRequest = new UpdateModificationRequest(this);
        updates.add(updateModificationRequest);
        return updateModificationRequest;
    }

    public UpdateModificationRequest update(String existingId) {
        UpdateModificationRequest updateModificationRequest = new UpdateModificationRequest(this, existingId);
        updates.add(updateModificationRequest);
        return updateModificationRequest;
    }

    @Override
    public String toXML() {
        return buildRequest(name).toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("add", adds)
            .addElement("remove", removeModificationIds)
            .addElement("update", updates);
    }
}
