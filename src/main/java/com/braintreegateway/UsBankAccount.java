package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class UsBankAccount implements PaymentMethod {
    private String routingNumber;
    private String last4;
    private String accountType;
    private String accountHolderName;
    private String token;
    private String imageUrl;
    private String bankName;
    private List<Subscription> subscriptions;
    private String customerId;
    private Boolean isDefault;
    private AchMandate achMandate;
    private List<UsBankAccountVerification> verifications;
    private Boolean isVerified;

    public UsBankAccount(NodeWrapper node) {
        this.routingNumber= node.findString("routing-number");
        this.last4 = node.findString("last-4");
        this.accountType = node.findString("account-type");
        this.accountHolderName = node.findString("account-holder-name");
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
        this.bankName = node.findString("bank-name");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        this.customerId = node.findString("customer-id");
        this.isDefault = node.findBoolean("default");
        NodeWrapper achMandateNode = node.findFirst("ach-mandate");
        if (achMandateNode != null) {
            this.achMandate = new AchMandate(achMandateNode);
        }

        this.verifications = new ArrayList<UsBankAccountVerification>();
        for (NodeWrapper verification : node.findAll("verifications/us-bank-account-verification")) {
            this.verifications.add(new UsBankAccountVerification(verification));
        }

        this.isVerified = node.findBoolean("verified");
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

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBankName() {
        return bankName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public AchMandate getAchMandate() {
        return achMandate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<UsBankAccountVerification> getVerifications() {
        return verifications;
    }

    public Boolean isVerified() {
        return isVerified;
    }
}
