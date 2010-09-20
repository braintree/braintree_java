package com.braintreegateway;

import java.util.Calendar;

public class DateRangeNode<T extends SearchRequest> extends SearchNode<T> {

    public DateRangeNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T between(Calendar min, Calendar max) {
        greaterThanOrEqualTo(min);
        lessThanOrEqualTo(max);
        return parent;
    }

    @Deprecated
    public T greaterThanOrEqual(Calendar min) {
        return greaterThanOrEqualTo(min);
    }

    @Deprecated
    public T lessThanOrEqual(Calendar max) {
        return lessThanOrEqualTo(max);
    }

    public T greaterThanOrEqualTo(Calendar min) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("min", min));
        return parent;
    }
    
    public T lessThanOrEqualTo(Calendar max) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("max", max));
        return parent;
    }
}
