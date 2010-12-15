package com.braintreegateway;

public class TransactionDescriptorRequest extends Request {
    private String name;
    private String phone;
    private TransactionRequest parent;

    public TransactionDescriptorRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionDescriptorRequest name(String name) {
        this.name = name;
        return this;
    }

    public TransactionDescriptorRequest phone(String phone) {
        this.phone = phone;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("descriptor").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("name", name).
            addElement("phone", phone);
    }
}
