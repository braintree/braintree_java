package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Deprecated
public class AmexExpressCheckoutCard implements PaymentMethod {
    private String cardType;
    private String token;
    private String bin;
    private String expirationMonth;
    private String expirationYear;
    private String cardMemberNumber;
    private String cardMemberExpiryDate;
    private String sourceDescription;
    private Boolean isDefault;
    private String imageUrl;
    private String customerId;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;

    public AmexExpressCheckoutCard(NodeWrapper node) {
        this.bin = node.findString("bin");
        this.cardMemberExpiryDate = node.findString("card-member-expiry-date");
        this.cardMemberNumber = node.findString("card-member-number");
        this.cardType = node.findString("card-type");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.sourceDescription = node.findString("source-description");
        this.token = node.findString("token");

        this.isDefault = node.findBoolean("default");
        this.imageUrl = node.findString("image-url");
        this.customerId = node.findString("customer-id");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }

    }

    public String getBin() {
        return bin;
    }

    public String getCardMemberExpiryDate() {
        return cardMemberExpiryDate;
    }

    public String getCardMemberNumber() {
        return cardMemberNumber;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getToken() {
        return token;
    }

    public boolean isDefault() {
        return isDefault;
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
}
