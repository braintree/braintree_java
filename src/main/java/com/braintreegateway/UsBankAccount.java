package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class UsBankAccount implements PaymentMethod {
    private String accountHolderName;
    private String accountType;
    private AchMandate achMandate;
    private String bankName;
    private String businessName;
    private Calendar createdAt;
    private String customerId;
    private String description;
    private String firstName;
    private String imageUrl;
    private String last4;
    private String lastName;
    private String ownerId;
    private String ownershipType;
    private Calendar plaidVerifiedAt;
    private String routingNumber;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;
    private List<UsBankAccountVerification> verifications;
    private Boolean isDefault;
    private boolean isVerifiable;
    private Boolean isVerified;
    

    public UsBankAccount(NodeWrapper node) {
        this.accountHolderName = node.findString("account-holder-name");
        this.accountType = node.findString("account-type");
        NodeWrapper achMandateNode = node.findFirst("ach-mandate");
        if (achMandateNode != null) {
            this.achMandate = new AchMandate(achMandateNode);
        }
        this.bankName = node.findString("bank-name");
        this.businessName = node.findString("business-name");
        this.createdAt = node.findDateTime("created-at");
        this.customerId = node.findString("customer-id");
        this.description = node.findString("description");
        this.firstName = node.findString("first-name");
        this.imageUrl = node.findString("image-url");
        this.last4 = node.findString("last-4");
        this.lastName = node.findString("last-name");
        this.ownerId = node.findString("owner-id");
        this.ownershipType = node.findString("ownership-type");
        this.plaidVerifiedAt = node.findDateTime("plaid-verified-at");
        this.routingNumber = node.findString("routing-number");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        this.token = node.findString("token");
        this.updatedAt = node.findDateTime("updated-at");
        this.verifications = new ArrayList<UsBankAccountVerification>();
        for (NodeWrapper verification : node.findAll("verifications/us-bank-account-verification")) {
            this.verifications.add(new UsBankAccountVerification(verification));
        }
        this.isDefault = node.findBoolean("default");
        this.isVerifiable = node.findBoolean("verifiable");
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

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDescription() {
        return description;
    }

    public String getFirstName() {
        return firstName;
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

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public Calendar getPlaidVerifiedAt() {
        return plaidVerifiedAt;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public String getToken() {
        return token;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public List<UsBankAccountVerification> getVerifications() {
        return verifications;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isVerifiable() {
        return isVerifiable;
    }

    public Boolean isVerified() {
        return isVerified;
    }
}
