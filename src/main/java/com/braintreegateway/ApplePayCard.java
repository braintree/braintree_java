package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class ApplePayCard implements PaymentMethod {
    private String imageUrl;
    private String token;
    private boolean isDefault;
    private String cardType;
    private String last4;
    private String expirationMonth;
    private String expirationYear;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;

    public ApplePayCard(NodeWrapper node) {
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.cardType = node.findString("card-type");
        this.last4 = node.findString("last-4");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }

    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getCardType() {
        return cardType;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
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

    public String getLast4() {
        return last4;
    }
}
