package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueNode<T extends SearchRequest, S> extends SearchNode<T> {
    public MultipleValueNode(String nodeName, T parent) {
        super(nodeName, parent);
    }
    
    public T in(List<S> items) {
        return assembleMultiValueCriteria(items);
    }
    
    public T in(S... items) {
        return in(Arrays.asList(items));
    }
    
    @SuppressWarnings("unchecked")
    public T is(S item) {
        return in(item);
    }
}
