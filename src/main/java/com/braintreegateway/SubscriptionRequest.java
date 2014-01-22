package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Provides a fluent interface to build up requests around {@link Subscription
 * Subscriptions}.
 */
public class SubscriptionRequest extends Request {
    private ModificationsRequest addOnsRequest;
    private Integer billingDayOfMonth;
    private SubscriptionDescriptorRequest descriptorRequest;
    private ModificationsRequest discountsRequest;
    private Calendar firstBillingDate;
    private Boolean hasTrialPeriod;
    private String id;
    private String merchantAccountId;
    private Boolean neverExpires;
    private Integer numberOfBillingCycles;
    private SubscriptionOptionsRequest options;
    private String paymentMethodToken;
    private String paymentMethodNonce;
    private String planId;
    private BigDecimal price;
    private Integer trialDuration;
    private Subscription.DurationUnit trialDurationUnit;

    public ModificationsRequest addOns() {
        addOnsRequest = new ModificationsRequest(this, "addOns");
        return addOnsRequest;
    }

    public SubscriptionRequest billingDayOfMonth(Integer billingDayOfMonth) {
        this.billingDayOfMonth = billingDayOfMonth;
        return this;
    }

    public SubscriptionDescriptorRequest descriptor() {
        descriptorRequest = new SubscriptionDescriptorRequest(this);
        return descriptorRequest;
    }

    public ModificationsRequest discounts() {
        discountsRequest = new ModificationsRequest(this, "discounts");
        return discountsRequest;
    }

    public SubscriptionRequest firstBillingDate(Calendar firstBillingDate) {
        this.firstBillingDate = firstBillingDate;
        return this;
    }

    public SubscriptionRequest id(String id) {
        this.id = id;
        return this;
    }

    public SubscriptionRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
        return this;
    }

    public SubscriptionRequest neverExpires(Boolean neverExpires) {
        this.neverExpires = neverExpires;
        return this;
    }

    public SubscriptionRequest numberOfBillingCycles(Integer numberOfBillingCycles) {
        this.numberOfBillingCycles = numberOfBillingCycles;
        return this;
    }

    public SubscriptionOptionsRequest options() {
        options = new SubscriptionOptionsRequest(this);
        return options;
    }

    public SubscriptionRequest paymentMethodToken(String token) {
        this.paymentMethodToken = token;
        return this;
    }

    public SubscriptionRequest paymentMethodNonce(String nonce) {
        this.paymentMethodNonce = nonce;
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

    @Override
    public String toXML() {
        return buildRequest("subscription").toXML();
    }

    public SubscriptionRequest trialDuration(Integer trialDuration) {
        this.trialDuration = trialDuration;
        return this;
    }

    public SubscriptionRequest trialDurationUnit(Subscription.DurationUnit trialDurationUnit) {
        this.trialDurationUnit = trialDurationUnit;
        return this;
    }

    public SubscriptionRequest trialPeriod(Boolean hasTrialPeriod) {
        this.hasTrialPeriod = hasTrialPeriod;
        return this;
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("id", id).
            addElement("addOns", addOnsRequest).
            addElement("billingDayOfMonth", billingDayOfMonth).
            addElement("descriptor", descriptorRequest).
            addElement("discounts", discountsRequest).
            addElement("firstBillingDate", firstBillingDate).
            addElement("merchantAccountId", merchantAccountId).
            addElement("neverExpires", neverExpires).
            addElement("numberOfBillingCycles", numberOfBillingCycles).
            addElement("options", options).
            addElement("paymentMethodToken", paymentMethodToken).
            addElement("paymentMethodNonce", paymentMethodNonce).
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
