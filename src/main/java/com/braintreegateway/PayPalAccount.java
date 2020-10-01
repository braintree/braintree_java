package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PayPalAccount implements PaymentMethod {
    private String email;
    private String token;
    private String billingAgreementId;
    private boolean isDefault;
    private String imageUrl;
    private String payerId;
    private String customerId;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;
    private Calendar revokedAt;

    public PayPalAccount(NodeWrapper node) {
        this.email = node.findString("email");
        this.token = node.findString("token");
        this.billingAgreementId = node.findString("billing-agreement-id");
        this.isDefault = node.findBoolean("default");
        this.imageUrl = node.findString("image-url");
        this.payerId = node.findString("payer-id");
        this.customerId = node.findString("customer-id");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        this.revokedAt = node.findDateTime("revoked-at");
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getBillingAgreementId() {
        return billingAgreementId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPayerId() {
        return payerId;
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

    public Calendar getRevokedAt() {
        return revokedAt;
    }
}
