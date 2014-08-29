package com.braintreegateway;

public class TransactionPayPalRequest extends Request {
    private TransactionRequest parent;
    private String payeeEmail;

    public TransactionPayPalRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    @Override
    public String toXML() {
        return buildRequest("paypalAccount").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paypalAccount");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).addElement("payeeEmail", payeeEmail);
    }
}
