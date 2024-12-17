package com.braintreegateway;


public class TransactionOptionsRecipientPhoneRequest extends Request {
    private TransactionOptionsPayPalRequest parent;
    private String countryCode;
    private String nationalNumber;

    public TransactionOptionsRecipientPhoneRequest(TransactionOptionsPayPalRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsPayPalRequest done() {
        return parent;
    }

    public TransactionOptionsRecipientPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public TransactionOptionsRecipientPhoneRequest nationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("recipientPhone").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("recipientPhone");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("countryCode", countryCode)
            .addElement("nationalNumber", nationalNumber);
        return builder;
    }
}
