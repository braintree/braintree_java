package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class MerchantAccount {

    public enum Status {
        PENDING,
        ACTIVE,
        SUSPENDED,
        UNRECOGNIZED
    }

    private final String id;
    private final Status status;
    private final String currencyIsoCode;
    private final Boolean isDefault;

    public MerchantAccount(NodeWrapper node) {
        NodeWrapper responseNode = node.findFirst("merchant-account");
        if (responseNode != null) {
            node = responseNode;
        }

        this.id = node.findString("id");
        this.currencyIsoCode = node.findString("currency-iso-code");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        this.isDefault = node.findBoolean("default");

    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public Boolean isDefault() {
        return isDefault;
    }

}

