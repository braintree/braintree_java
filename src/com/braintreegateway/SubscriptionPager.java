package com.braintreegateway;

public class SubscriptionPager implements Pager<Subscription> {
    private SubscriptionGateway gateway;
    private SubscriptionSearchRequest search;

    public SubscriptionPager(SubscriptionGateway gateway, SubscriptionSearchRequest search) {
        this.gateway = gateway;
        this.search = search;
    }

    public PagedCollection<Subscription> getPage(int page) {
        return gateway.search(search, page);
    }
}
