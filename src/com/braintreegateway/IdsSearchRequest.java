package com.braintreegateway;

public class IdsSearchRequest extends SearchRequest {
    public MultipleValueNode<IdsSearchRequest> ids() {
        return new MultipleValueNode<IdsSearchRequest>("ids", this);
    }
}
