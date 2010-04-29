package com.braintreegateway;

public class PartialMatchNode<T extends SearchRequest> extends EqualityNode<T> {
    public PartialMatchNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T endsWith(String value) {
        return assembleCriteria("ends_with", value);
    }
    
    public T startsWith(String value) {
        return assembleCriteria("starts_with", value);
    }
}
