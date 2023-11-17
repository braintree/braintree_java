package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class UsBankAccountDetails {
    private String accountHolderName;
    private String accountType;
    private AchMandate achMandate;
    private String bankName;
    private String businessName;
    private String firstName;
    private String globalId;
    private String imageUrl;
    private String last4;
    private String lastName;
    private String ownershipType;
    private String routingNumber;
    private String token;
    private boolean isVerified;

    public UsBankAccountDetails(NodeWrapper node) {
        this.accountHolderName = node.findString("account-holder-name");
        this.accountType = node.findString("account-type");
        this.achMandate = new AchMandate(node.findFirst("ach-mandate"));
        this.bankName = node.findString("bank-name");
        this.businessName = node.findString("business-name");
        this.firstName = node.findString("first-name");
        this.globalId = node.findString("global-id");
        this.imageUrl = node.findString("image-url");
        this.last4 = node.findString("last-4");
        this.lastName = node.findString("last-name");
        this.ownershipType = node.findString("ownership-type");
        this.routingNumber = node.findString("routing-number");
        this.token = node.findString("token");
        this.isVerified = node.findBoolean("verified");
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountType() {
        return accountType;
    }

    public AchMandate getAchMandate() {
        return achMandate;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGlobalId() {
        return globalId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLast4() {
        return last4;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public String getToken() {
        return token;
    }

    public boolean isVerified() {
        return isVerified;
    }
}
