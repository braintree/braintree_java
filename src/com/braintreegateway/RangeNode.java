package com.braintreegateway;

import java.math.BigDecimal;

public class RangeNode<T extends SearchRequest> extends SearchNode<T> {

    public RangeNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T between(BigDecimal min, BigDecimal max) {
        greaterThanOrEqual(min);
        lessThanOrEqual(max);
        return parent;
    }

    public T greaterThanOrEqual(BigDecimal min) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("min", min.toString()));
        return parent;
    }

    public T lessThanOrEqual(BigDecimal max) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("max", max.toString()));
        return parent;
    }

}
