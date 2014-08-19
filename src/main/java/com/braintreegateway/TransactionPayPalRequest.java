package com.braintreegateway;

public class TransactionPayPalRequest extends Request {
    private TransactionRequest parent;
    private String payeeEmail;
    private String bnCode;

    public TransactionPayPalRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionPayPalRequest payeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
        return this;
    }

    public TransactionPayPalRequest bnCode(String bnCode) {
        this.bnCode = bnCode;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public String getBnCode() {
      return bnCode;
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
            addElement("payeeEmail", payeeEmail).
            addElement("bnCode", bnCode);
    }
}
