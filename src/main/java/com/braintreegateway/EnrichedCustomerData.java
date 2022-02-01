package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class EnrichedCustomerData {
    private List<String> fieldsUpdated;
    private VenmoProfileData profileData;

    public EnrichedCustomerData(NodeWrapper node) {
        this.fieldsUpdated = new ArrayList<String>();
        for (NodeWrapper field : node.findAll("fields-updated/item")) {
            fieldsUpdated.add(field.findString("."));
        }

        NodeWrapper venmoProfileDataNode = node.findFirst("profile-data");
        this.profileData = new VenmoProfileData(venmoProfileDataNode);
    }

    public List<String> getFieldsUpdated() {
        return fieldsUpdated;
    }

    public VenmoProfileData getProfileData() {
        return profileData;
    }
 }
