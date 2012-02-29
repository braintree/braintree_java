package com.braintreegateway;

public class EqualityNode<T extends SearchRequest> extends IsNode<T> {
    public EqualityNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T isNot(String value) {
        return assembleCriteria("is_not", value);
    }
}