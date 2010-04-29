package com.braintreegateway;

public class TextNode<T extends SearchRequest> extends SearchNode<T> {
    public TextNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T is(String value) {
        return assembleCriteria("is", value);
    }

    public T isNot(String value) {
        return assembleCriteria("is_not", value);
    }

    public T endsWith(String value) {
        return assembleCriteria("ends_with", value);
    }
    
    public T startsWith(String value) {
        return assembleCriteria("starts_with", value);
    }

    public T contains(String value) {
        return assembleCriteria("contains", value);
    }
}
