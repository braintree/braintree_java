package com.braintreegateway;

public class EqualityNode<T extends SearchRequest> extends SearchNode<T> {
    public EqualityNode(String nodeName, T parent) {
        super(nodeName, parent);
    }
    
    public T is(String value) {
        return assembleCriteria("is", value);
    }

    public T isNot(String value) {
        return assembleCriteria("is_not", value);
    }
}
