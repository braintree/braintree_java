package com.braintreegateway;

import java.util.Calendar;

public class DateRangeNode<T extends SearchRequest> extends SearchNode<T> {

    public DateRangeNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T between(Calendar min, Calendar max) {
        greaterThanOrEqual(min);
        lessThanOrEqual(max);
        return parent;
    }

    public T greaterThanOrEqual(Calendar min) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("min", min));
        return parent;
    }

    public T lessThanOrEqual(Calendar max) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("max", max));
        return parent;
    }

}
