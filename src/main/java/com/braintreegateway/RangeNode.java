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
        greaterThanOrEqualTo(min);
        lessThanOrEqualTo(max);
        return parent;
    }

    public T greaterThanOrEqualTo(BigDecimal min) {
        return greaterThanOrEqualTo(min.toString());
    }

    public T greaterThanOrEqualTo(int min) {
        return greaterThanOrEqualTo(String.valueOf(min));
    }

    public T greaterThanOrEqualTo(String min) {
        parent.addRangeCriteria(nodeName, new SearchCriteria("min", min));
        return parent;
    }

    public T lessThanOrEqualTo(BigDecimal max) {
        return lessThanOrEqualTo(max.toString());
    }

    public T lessThanOrEqualTo(int max) {
        return lessThanOrEqualTo(String.valueOf(max));
    }

    public T lessThanOrEqualTo(String max) {
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
