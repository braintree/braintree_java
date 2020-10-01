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

    public enum FundingDestination {
        BANK("bank"),
        MOBILE_PHONE("mobile_phone"),
        EMAIL("email"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        FundingDestination(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final String id;
    private final Status status;
    private final MerchantAccount masterMerchantAccount;
    private final IndividualDetails individualDetails;
    private final BusinessDetails businessDetails;
    private final FundingDetails fundingDetails;
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

        NodeWrapper masterNode = node.findFirst("master-merchant-account");
        if (masterNode != null) {
            this.masterMerchantAccount = new MerchantAccount(masterNode);
        } else {
            this.masterMerchantAccount = null;
        }

        NodeWrapper individualNode = node.findFirst("individual");
        if (individualNode != null) {
            this.individualDetails = new IndividualDetails(individualNode);
        } else {
            this.individualDetails = null;
        }

        NodeWrapper businessNode = node.findFirst("business");
        if (businessNode != null) {
            this.businessDetails = new BusinessDetails(businessNode);
        } else {
            this.businessDetails = null;
        }

        NodeWrapper fundingNode = node.findFirst("funding");
        if (fundingNode != null) {
            this.fundingDetails = new FundingDetails(fundingNode);
        } else {
            this.fundingDetails = null;
        }
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

    public IndividualDetails getIndividualDetails() {
        return individualDetails;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public FundingDetails getFundingDetails() {
        return fundingDetails;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public boolean isSubMerchant() {
        return masterMerchantAccount != null;
    }
}

