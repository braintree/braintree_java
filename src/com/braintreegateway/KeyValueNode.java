package com.braintreegateway;

public class KeyValueNode<T extends SearchRequest> extends SearchNode<T> {
    public KeyValueNode(String nodeName, T parent) {
        super(nodeName, parent);
    }
     
    public T is(Object value) {
        parent.addKeyValueCriteria(nodeName.toString(), value.toString());
        return parent;
    }
}
