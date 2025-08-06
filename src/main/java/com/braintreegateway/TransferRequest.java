package com.braintreegateway;

public class TransferRequest extends Request {

    private TransactionRequest parent;
    private String type;

    public TransferRequest() {
    }

    public TransferRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransferRequest type(String type ) {
        this.type = type;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("transfer").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("transfer");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);
        if (type != null) {
            builder.addElement("type", type);
        }
        return builder;
    }
}
