package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link Subscription} searches.
 */
public class SubscriptionSearchRequest extends SearchRequest {
    public SubscriptionSearchRequest() {
        super();
    }
    
    public TextNode<SubscriptionSearchRequest> planId() {
        return new TextNode<SubscriptionSearchRequest>("plan_id", this);
    }
    
    public TextNode<SubscriptionSearchRequest> daysPastDue() {
        return new TextNode<SubscriptionSearchRequest>("days_past_due", this);
    }

    public MultipleValueNode<SubscriptionSearchRequest> ids() {
        return new MultipleValueNode<SubscriptionSearchRequest>("ids", this);
    }

    public MultipleValueNode<SubscriptionSearchRequest> status() {
        return new MultipleValueNode<SubscriptionSearchRequest>("status", this);
    }    
}
