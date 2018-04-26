package com.braintreegateway;

public class EndsWithNode<T extends SearchRequest> extends SearchNode<T> {
    public EndsWithNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T endsWith(String value) {
        return assembleCriteria("ends_with", value);
    }
}
