package com.braintreegateway;

public class IsNode<T extends SearchRequest> extends SearchNode<T> {
    public IsNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T is(String value) {
        return assembleCriteria("is", value);
    }
}