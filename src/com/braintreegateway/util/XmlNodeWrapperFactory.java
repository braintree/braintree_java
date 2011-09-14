package com.braintreegateway.util;

public class XmlNodeWrapperFactory implements NodeWrapperFactory {
    public NodeWrapper create(String xml) {
        return new XmlNodeWrapper(xml);
    }
}
