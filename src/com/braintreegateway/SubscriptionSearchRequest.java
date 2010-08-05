package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link Subscription}
 * searches.
 */
public class SubscriptionSearchRequest extends SearchRequest {
    public SubscriptionSearchRequest() {
        super();
    }

    public TextNode<SubscriptionSearchRequest> daysPastDue() {
        return new TextNode<SubscriptionSearchRequest>("days_past_due", this);
    }

    public TextNode<SubscriptionSearchRequest> id() {
        return new TextNode<SubscriptionSearchRequest>("id", this);
    }

    public MultipleValueNode<SubscriptionSearchRequest, String> ids() {
        return new MultipleValueNode<SubscriptionSearchRequest, String>("ids", this);
    }

    public MultipleValueNode<SubscriptionSearchRequest, String> merchantAccountId() {
        return new MultipleValueNode<SubscriptionSearchRequest, String>("merchant_account_id", this);
    }

    public TextNode<SubscriptionSearchRequest> planId() {
        return new TextNode<SubscriptionSearchRequest>("plan_id", this);
    }

    public RangeNode<SubscriptionSearchRequest> price() {
        return new RangeNode<SubscriptionSearchRequest>("price", this);
    }

    public MultipleValueNode<SubscriptionSearchRequest, Subscription.Status> status() {
        return new MultipleValueNode<SubscriptionSearchRequest, Subscription.Status>("status", this);
    }

    public RangeNode<SubscriptionSearchRequest> billingCyclesRemaining() {
        return new RangeNode<SubscriptionSearchRequest>("billing_cycles_remaining", this);
    }
}
