package com.braintreegateway;

import java.util.List;

public class SearchNode {
    private String nodeName;
    private SubscriptionSearchRequest parent;

    public SearchNode(String nodeName, SubscriptionSearchRequest parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }

    protected SubscriptionSearchRequest assembleCriteria(String operation, String value) {
        this.parent.addCriteria(this.nodeName, new SearchCriteria(operation, value));
        return this.parent;
    }
    
    protected SubscriptionSearchRequest assembleMultiValueCriteria(List<?> items) {
        this.parent.addMultipleValueCriteria(this.nodeName, new SearchCriteria(items));
        return this.parent;
    }
}
