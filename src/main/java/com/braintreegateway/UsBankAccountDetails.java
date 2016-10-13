package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class UsBankAccountDetails {
    private String routingNumber;
    private String last4;
    private String accountType;
    private String accountDescription;
    private String accountHolderName;
    private String token;
    private String imageUrl;

    public UsBankAccountDetails(NodeWrapper node) {
        this.routingNumber= node.findString("routing-number");
        this.last4 = node.findString("last-4");
        this.accountType = node.findString("account-type");
        this.accountDescription = node.findString("account-description");
        this.accountHolderName = node.findString("account-holder-name");
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getLast4() {
        return last4;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
