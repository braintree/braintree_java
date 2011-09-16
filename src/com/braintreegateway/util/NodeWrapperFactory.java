package com.braintreegateway.util;

public interface NodeWrapperFactory {

    NodeWrapperFactory instance = new MapNodeWrapperFactory();

    NodeWrapper create(String xml);
}
