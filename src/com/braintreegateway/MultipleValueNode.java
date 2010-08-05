package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueNode<T extends SearchRequest, U> extends SearchNode<T> {
    public MultipleValueNode(String nodeName, T parent) {
        super(nodeName, parent);
    }
    
    public T in(List<U> items) {
        return assembleMultiValueCriteria(items);
    }
    
    public T in(U... items) {
        return in(Arrays.asList(items));
    }
    
    @SuppressWarnings("unchecked")
    public T is(U item) {
        return in(item);
    }
}
