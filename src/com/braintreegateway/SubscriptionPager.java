package com.braintreegateway;

import java.util.List;

public class SubscriptionPager implements Pager<Subscription> {
    private SubscriptionGateway gateway;
    private SubscriptionSearchRequest search;

    public SubscriptionPager(SubscriptionGateway gateway, SubscriptionSearchRequest search) {
        this.gateway = gateway;
        this.search = search;
    }

    public List<Subscription> getPage(List<String> ids) {
        return gateway.fetchSubscriptions(search, ids);
    }
}
