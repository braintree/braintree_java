package com.braintreegateway;

public class TransactionOptionsPayPalRequest extends Request {
    private TransactionOptionsRequest parent;
    private String payeeEmail;

    public TransactionOptionsPayPalRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paypal").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paypal");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("payeeEmail", payeeEmail);
    }
}
