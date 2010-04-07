package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueNode extends SearchNode {
    public MultipleValueNode(String nodeName, SubscriptionSearchRequest parent) {
        super(nodeName, parent);
    }

    public SubscriptionSearchRequest in(List<?> items) {
        return assembleMultiValueCriteria(items);
    }
    
    public SubscriptionSearchRequest in(Object... items) {
        return assembleMultiValueCriteria(Arrays.asList(items));
    }
}
