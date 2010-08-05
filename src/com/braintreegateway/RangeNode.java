package com.braintreegateway;

import java.math.BigDecimal;

public class RangeNode<T extends SearchRequest> extends SearchNode<T> {

    public RangeNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T between(BigDecimal min, BigDecimal max) {
        return between(min.toString(), max.toString());
    }

    public T between(int min, int max) {
        return between(String.valueOf(min), String.valueOf(max));
    }

    public T between(String min, String max) {
        greaterThanOrEqual(min);
        lessThanOrEqual(max);
        return parent;
    }

    public T greaterThanOrEqual(BigDecimal min) {
        return greaterThanOrEqual(min.toString());
    }

    public T greaterThanOrEqual(int min) {
        return greaterThanOrEqual(String.valueOf(min));
    }

    public T greaterThanOrEqual(String min) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("min", min));
        return parent;
    }

    public T lessThanOrEqual(BigDecimal max) {
        return lessThanOrEqual(max.toString());
    }

    public T lessThanOrEqual(int max) {
        return lessThanOrEqual(String.valueOf(max));
    }

    public T lessThanOrEqual(String max) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("max", max));
        return parent;
    }

    public T is(BigDecimal value) {
        return is(value.toString());
    }

    public T is(int value) {
        return is(String.valueOf(value));
    }

    public T is(String value) {
        return assembleCriteria("is", value);
    }
}
