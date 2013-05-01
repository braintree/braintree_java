package com.braintreegateway;

public class TextNode<T extends SearchRequest> extends PartialMatchNode<T> {
    public TextNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T contains(String value) {
        return assembleCriteria("contains", value);
    }
}
