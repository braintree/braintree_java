package com.braintreegateway;

public class CustomerSearchRequest extends SearchRequest {
    public MultipleValueNode<CustomerSearchRequest> ids() {
        return new MultipleValueNode<CustomerSearchRequest>("ids", this);
    }
}
