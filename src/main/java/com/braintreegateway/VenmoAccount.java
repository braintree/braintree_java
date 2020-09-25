package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VenmoAccount implements PaymentMethod {
    private String token;
    private String username;
    private String venmoUserId;
    private String sourceDescription;
    private String imageUrl;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;
    private String customerId;
    private Boolean isDefault;

    public VenmoAccount(NodeWrapper node) {
        this.token = node.findString("token");
        this.username = node.findString("username");
        this.venmoUserId = node.findString("venmo-user-id");
        this.sourceDescription = node.findString("source-description");
        this.imageUrl = node.findString("image-url");

        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }

        this.customerId = node.findString("customer-id");
        this.isDefault = node.findBoolean("default");
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getVenmoUserId() {
        return venmoUserId;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public String getCustomerId() {
        return customerId;
    }

    public boolean isDefault() {
        return isDefault;
    }
}

