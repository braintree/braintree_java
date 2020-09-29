package com.braintreegateway;

public class TransactionApplePayCardRequest extends Request {

    private String number;
    private String cardholderName;
    private String cryptogram;
    private String expirationMonth;
    private String expirationYear;
    private String eciIndicator;
    private TransactionRequest parent;

    public TransactionApplePayCardRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionApplePayCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public TransactionApplePayCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public TransactionApplePayCardRequest cryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
        return this;
    }

    public TransactionApplePayCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public TransactionApplePayCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public TransactionApplePayCardRequest eciIndicator(String eciIndicator) {
        this.eciIndicator = eciIndicator;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("applePayCard").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("applePayCard");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
                .addElement("number", number)
                .addElement("cardholderName", cardholderName)
                .addElement("cryptogram", cryptogram)
                .addElement("expirationMonth", expirationMonth)
                .addElement("expirationYear", expirationYear)
                .addElement("eciIndicator", eciIndicator);
    }
}
