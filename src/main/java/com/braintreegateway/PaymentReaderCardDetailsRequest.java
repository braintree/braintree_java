package com.braintreegateway;

public class PaymentReaderCardDetailsRequest extends Request {
    private String encryptedCardData;
    private String keySerialNumber;
    private TransactionCreditCardRequest parent;

    public PaymentReaderCardDetailsRequest(TransactionCreditCardRequest parent) {
        this.parent = parent;
    }

    public PaymentReaderCardDetailsRequest encryptedCardData(String encryptedCardData) {
        this.encryptedCardData = encryptedCardData;
        return this;
    }

    public PaymentReaderCardDetailsRequest keySerialNumber(String keySerialNumber) {
        this.keySerialNumber = keySerialNumber;
        return this;
    }

    public TransactionCreditCardRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("paymentReaderCardDetails").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("paymentReaderCardDetails");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("encryptedCardData", encryptedCardData)
            .addElement("keySerialNumber", keySerialNumber);
    }
}
