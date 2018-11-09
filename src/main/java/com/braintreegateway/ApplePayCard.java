package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class ApplePayCard implements PaymentMethod {
    private String imageUrl;
    private String token;
    private boolean isDefault;
    private String bin;
    private String cardType;
    private String paymentInstrumentName;
    private String sourceDescription;
    private String last4;
    private String expirationMonth;
    private String expirationYear;
    private boolean expired;
    private String customerId;
    private String cardholderName;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;

    public ApplePayCard(NodeWrapper node) {
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.bin = node.findString("bin");
        this.cardType = node.findString("card-type");
        this.paymentInstrumentName = node.findString("payment-instrument-name");
        this.sourceDescription = node.findString("source-description");
        this.last4 = node.findString("last-4");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.expired = node.findBoolean("expired");
        this.customerId = node.findString("customer-id");
        this.cardholderName = node.findString("cardholder-name");
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

    public String getBin() {
        return bin;
    }

    public String getCardType() {
        return cardType;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public boolean getExpired() {
        return expired;
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

    public String getCustomerId() {
        return customerId;
    }

    public String getCardholderName() {
        return cardholderName;
    }
}
