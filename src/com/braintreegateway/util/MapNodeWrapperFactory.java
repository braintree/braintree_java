package com.braintreegateway.util;

public class MapNodeWrapperFactory implements NodeWrapperFactory {
    public NodeWrapper create(String xml) {
        return MapNodeWrapper.parse(xml);
    }
}
