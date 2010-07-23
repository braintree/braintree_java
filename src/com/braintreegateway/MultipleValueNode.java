package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueNode<T extends SearchRequest> extends SearchNode<T> {
    private List<?> allowedValues;
    
    public MultipleValueNode(String nodeName, T parent) {
        super(nodeName, parent);
    }
    
    public MultipleValueNode(String nodeName, T parent, Object[] allowedValues) {
        super(nodeName, parent);
        this.allowedValues = Arrays.asList(allowedValues);
    }

    public T in(List<?> items) {
        checkForValidItems(items);
        return assembleMultiValueCriteria(items);
    }
    
    public T in(Object... items) {
        return in(Arrays.asList(items));
    }
    
    public T is(Object item) {
        return in(item);
    }
    
    private void checkForValidItems(List<?> items) {
        if (allowedValues == null) return;

        for (Object item : items) {
            if (!allowedValues.contains(item)) {
                throw new IllegalArgumentException(String.format("The %s node does not accept %s as a value.", nodeName, item));
            }
        }
    }
}
