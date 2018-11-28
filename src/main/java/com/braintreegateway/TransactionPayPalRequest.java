package com.braintreegateway;

public class TransactionPayPalRequest extends Request {
    private TransactionRequest parent;
    private String payeeId;
    private String payeeEmail;
    private String payerId;
    private String paymentId;

    public TransactionPayPalRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionPayPalRequest payeeId(String payeeId) {
        this.payeeId = payeeId;
        return this;
    }

    public TransactionPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public TransactionPayPalRequest payerId(String payerId) {
        this.payerId = payerId;
        return this;
    }

    public TransactionPayPalRequest paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPaymentId() {
        return paymentId;
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
        return new RequestBuilder(root).
            addElement("payeeId", payeeId).
            addElement("payeeEmail", payeeEmail).
            addElement("payerId", payerId).
            addElement("paymentId", paymentId);
    }
}
