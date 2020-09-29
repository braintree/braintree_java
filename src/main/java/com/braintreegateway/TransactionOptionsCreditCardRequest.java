package com.braintreegateway;


public class TransactionOptionsCreditCardRequest extends Request {
    private TransactionOptionsRequest parent;
    private String accountType;

    public TransactionOptionsCreditCardRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsCreditCardRequest accountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("creditCard").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("creditCard");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("accountType", accountType);
        return builder;
    }
}
