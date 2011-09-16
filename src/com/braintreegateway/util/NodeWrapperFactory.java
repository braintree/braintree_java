package com.braintreegateway.util;

public interface NodeWrapperFactory {

    NodeWrapperFactory instance = new XmlNodeWrapperFactory();

    NodeWrapper create(String xml);
}
