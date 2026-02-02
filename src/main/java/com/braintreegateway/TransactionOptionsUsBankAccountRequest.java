package com.braintreegateway;

public class TransactionOptionsUsBankAccountRequest extends Request {
    private TransactionOptionsRequest parent;
    private String achType;

    public TransactionOptionsUsBankAccountRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsUsBankAccountRequest achType(String achType) {
        this.achType = achType;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("usBankAccount").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("usBankAccount");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("achType", achType);
    }
}
