package com.braintreegateway;

import java.math.BigDecimal;

/**
 * Provides a fluent interface to build up requests around {@link Subscription Subscriptions}.
 */
public class SubscriptionRequest extends Request {
    private Boolean hasTrialPeriod;
    private String id;
    private String merchantAccountId;
    private String paymentMethodToken;
    private String planId;
    private BigDecimal price;
    private Integer trialDuration;
    private Subscription.DurationUnit trialDurationUnit;
    
    public SubscriptionRequest id(String id) {
        this.id = id;
        return this;
    }
    
    public SubscriptionRequest merchantAccountId(String merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
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
    
    public String toQueryString() {
        return "not implemented";
    }

    public String toQueryString(String root) {
        return "not implemented";
    }
    
    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append("<subscription>");
        builder.append(buildXMLElement("id", id));
        builder.append(buildXMLElement("merchantAccountId", merchantAccountId));
        builder.append(buildXMLElement("paymentMethodToken", paymentMethodToken));
        builder.append(buildXMLElement("planId", planId));
        builder.append(buildXMLElement("price", price));
        builder.append(buildXMLElement("trialPeriod", hasTrialPeriod));
        builder.append(buildXMLElement("trialDuration", trialDuration));
        if (trialDurationUnit != null) {
            builder.append(buildXMLElement("trialDurationUnit", trialDurationUnit.toString().toLowerCase()));
        }
        builder.append("</subscription>");
        return builder.toString();
    }
}
