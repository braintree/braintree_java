package com.braintreegateway;

public class IdsSearchRequest extends SearchRequest {
    public MultipleValueNode<IdsSearchRequest, String> ids() {
        return new MultipleValueNode<IdsSearchRequest, String>("ids", this);
    }
}
