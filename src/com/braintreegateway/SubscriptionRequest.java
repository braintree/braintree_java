package com.braintreegateway;

import java.math.BigDecimal;

/**
 * Provides a fluent interface to build up requests around {@link Subscription
 * Subscriptions}.
 */
public class SubscriptionRequest extends Request {
    private Boolean hasTrialPeriod;
    private String id;
    private String merchantAccountId;
    private Boolean neverExpires;
    private Integer numberOfBillingCycles;
    private String paymentMethodToken;
    private String planId;
    private BigDecimal price;
    private Integer trialDuration;
    private Subscription.DurationUnit trialDurationUnit;
    private ModificationsRequest addOnsRequest;
    private ModificationsRequest discountsRequest;

    public SubscriptionRequest id(String id) {
        this.id = id;
        return this;
    }

    public ModificationsRequest addOns() {
        addOnsRequest = new ModificationsRequest(this, "addOns");
        return addOnsRequest;
    }

    public ModificationsRequest discounts() {
        discountsRequest = new ModificationsRequest(this, "discounts");
        return discountsRequest;
    }

    public SubscriptionRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public SubscriptionRequest neverExpires(boolean neverExpires) {
        this.neverExpires = neverExpires;
        return this;
    }

    public SubscriptionRequest numberOfBillingCycles(Integer numberOfBillingCycles) {
        this.numberOfBillingCycles = numberOfBillingCycles;
        return this;
    }

    public SubscriptionRequest paymentMethodToken(String token) {
        this.paymentMethodToken = token;
        return this;
    }

    public SubscriptionRequest planId(String id) {
        this.planId = id;
        return this;
    }

    public SubscriptionRequest price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public SubscriptionRequest trialDuration(int trialDuration) {
        this.trialDuration = trialDuration;
        return this;
    }

    public SubscriptionRequest trialDurationUnit(Subscription.DurationUnit trialDurationUnit) {
        this.trialDurationUnit = trialDurationUnit;
        return this;
    }

    public SubscriptionRequest trialPeriod(boolean hasTrialPeriod) {
        this.hasTrialPeriod = hasTrialPeriod;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("subscription").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("id", id).
            addElement("addOns", addOnsRequest).
            addElement("discounts", discountsRequest).
            addElement("merchantAccountId", merchantAccountId).
            addElement("neverExpires", neverExpires).
            addElement("numberOfBillingCycles", numberOfBillingCycles).
            addElement("paymentMethodToken", paymentMethodToken).
            addElement("planId", planId).
            addElement("price", price).
            addElement("trialPeriod", hasTrialPeriod).
            addElement("trialDuration", trialDuration);

        if (trialDurationUnit != null) {
            builder.addElement("trialDurationUnit", trialDurationUnit.toString().toLowerCase());
        }

        return builder;
    }
}
