package com.braintreegateway;

public class TransactionOptionsThreeDSecureRequest extends Request {
    private TransactionOptionsRequest parent;
    private boolean required;

    public TransactionOptionsThreeDSecureRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsThreeDSecureRequest required(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("three-d-secure").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("three-d-secure");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("required", required);
    }
}
