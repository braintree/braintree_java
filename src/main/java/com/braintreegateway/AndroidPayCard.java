package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// NEXT_MAJOR_VERSION - rename this to GooglePayCard
public class AndroidPayCard implements PaymentMethod {
    private String cardType;
    private String last4;
    private String sourceCardType;
    private String sourceCardLast4;
    private String sourceDescription;
    private String virtualCardType;
    private String virtualCardLast4;
    private String expirationMonth;
    private String expirationYear;
    private String token;
    private String googleTransactionId;
    private String bin;
    private Boolean isDefault;
    private Boolean isNetworkTokenized;
    private String imageUrl;
    private String customerId;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;

    public AndroidPayCard(NodeWrapper node) {
        this.sourceCardType = node.findString("source-card-type");
        this.sourceCardLast4 = node.findString("source-card-last-4");
        this.sourceDescription = node.findString("source-description");
        this.virtualCardType = node.findString("virtual-card-type");
        this.virtualCardLast4 = node.findString("virtual-card-last-4");
        this.cardType = this.virtualCardType;
        this.last4 = this.virtualCardLast4;
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.token = node.findString("token");
        this.googleTransactionId = node.findString("google-transaction-id");
        this.bin = node.findString("bin");
        this.isDefault = node.findBoolean("default");
        this.imageUrl = node.findString("image-url");
        this.isNetworkTokenized = node.findBoolean("is-network-tokenized");
        this.customerId = node.findString("customer-id");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }

    }

    public String getCardType() {
        return cardType;
    }

    public String getLast4() {
        return last4;
    }

    public String getSourceCardType() {
        return sourceCardType;
    }

    public String getSourceCardLast4() {
        return sourceCardLast4;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getVirtualCardType() {
        return virtualCardType;
    }

    public String getVirtualCardLast4() {
        return virtualCardLast4;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getToken() {
        return token;
    }

    public String getGoogleTransactionId() {
        return googleTransactionId;
    }

    public String getBin() {
        return bin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isNetworkTokenized() {
        return isNetworkTokenized;
    }
}
