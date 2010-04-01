package com.braintreegateway;

public class TextNode extends SearchNode {

    private String nodeName;
    private SubscriptionSearchRequest parent;

    public TextNode(String nodeName, SubscriptionSearchRequest parent) {
        this.nodeName = nodeName;
        this.parent = parent;
    }

    public SubscriptionSearchRequest is(String value) {
        return assembleCriteria("is", value);
    }

    public SubscriptionSearchRequest isNot(String value) {
        return assembleCriteria("is_not", value);
    }

    public SubscriptionSearchRequest endsWith(String value) {
        return assembleCriteria("ends_with", value);
    }
    
    public SubscriptionSearchRequest startsWith(String value) {
        return assembleCriteria("starts_with", value);
    }

    public SubscriptionSearchRequest contains(String value) {
        return assembleCriteria("contains", value);
    }
    
    private SubscriptionSearchRequest assembleCriteria(String operation, String value) {
        this.parent.addCriteria(this.nodeName, new SearchCriteria(operation, value));
        return this.parent;
    }
}
