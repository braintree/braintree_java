package com.braintreegateway;

public class SubscriptionDescriptorRequest extends DescriptorRequest {
    private SubscriptionRequest parent;

    public SubscriptionDescriptorRequest(SubscriptionRequest parent) {
        this.parent = parent;
    }

    public SubscriptionDescriptorRequest name(String name) {
        super.name(name);
        return this;
    }

    public SubscriptionDescriptorRequest phone(String phone) {
        super.phone(phone);
        return this;
    }

    public SubscriptionRequest done() {
        return parent;
    }
}
