package com.braintreegateway.util;

public class SimpleNodeWrapperFactory implements NodeWrapperFactory {
    public NodeWrapper create(String xml) {
        return SimpleNodeWrapper.parse(xml);
    }
}
