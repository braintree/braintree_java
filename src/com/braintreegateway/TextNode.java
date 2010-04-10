package com.braintreegateway;

public class TextNode extends SearchNode {
    public TextNode(String nodeName, SubscriptionSearchRequest parent) {
        super(nodeName, parent);
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
}
