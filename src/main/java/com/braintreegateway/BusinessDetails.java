package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class BusinessDetails {
    private final String dbaName;
    private final String legalName;
    private final String taxId;
    private final Address address;

    public BusinessDetails(NodeWrapper node) {
        dbaName = node.findString("dba-name");
        legalName = node.findString("legal-name");
        taxId = node.findString("tax-id");
        NodeWrapper addressNode = node.findFirst("address");
        if (addressNode != null)
            this.address = new Address(addressNode);
        else
            this.address = null;
    }

    public String getDbaName() {
        return dbaName;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getTaxId() {
        return taxId;
    }

    public Address getAddress() {
        return address;
    }
}
