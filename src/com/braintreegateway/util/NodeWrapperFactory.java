package com.braintreegateway.util;

public interface NodeWrapperFactory {
    NodeWrapperFactory instance = new SimpleNodeWrapperFactory();

    NodeWrapper create(String xml);
}
