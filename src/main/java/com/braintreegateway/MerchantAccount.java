package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class MerchantAccount {

    public enum Status {
        PENDING,
        ACTIVE,
        SUSPENDED
    }

    private final String id;
    private final Status status;
    private final MerchantAccount masterMerchantAccount;

    public MerchantAccount(NodeWrapper node) {
        this.id = node.findString("id");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"));

        NodeWrapper masterNode = node.findFirst("master-merchant-account");
        if (masterNode != null)
            this.masterMerchantAccount = new MerchantAccount(masterNode);
        else
            this.masterMerchantAccount = null;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * @return the master merchant account. Will be null if this merchant account is a master
     * merchant account.
     */
    public MerchantAccount getMasterMerchantAccount() {
        return masterMerchantAccount;
    }

    public boolean isSubMerchant() {
        return masterMerchantAccount != null;
    }
}

