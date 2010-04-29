package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueNode<T extends SearchRequest> extends SearchNode<T> {
    public MultipleValueNode(Object nodeName, T parent) {
        super(nodeName, parent);
    }

    public T in(List<?> items) {
        return assembleMultiValueCriteria(items);
    }
    
    public T in(Object... items) {
        return assembleMultiValueCriteria(Arrays.asList(items));
    }
    
    public T is(Object item) {
        return in(item);
    }
}
