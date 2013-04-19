package com.braintreegateway;

import java.util.Arrays;
import java.util.List;

public class MultipleValueOrTextNode<T extends SearchRequest, S> extends TextNode<T> {
    public MultipleValueOrTextNode(String nodeName, T parent) {
        super(nodeName, parent);
    }

    public T in(List<S> items) {
        return assembleMultiValueCriteria(items);
    }

    public T in(S... items) {
        return in(Arrays.asList(items));
    }
}
