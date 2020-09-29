package com.braintreegateway;

public class TransactionCreditCardRequest extends Request {
    private String cardholderName;
    private String cvv;
    private String expirationDate;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private TransactionRequest parent;
    private String token;

    public TransactionCreditCardRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionCreditCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public TransactionCreditCardRequest cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    public TransactionCreditCardRequest expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public TransactionCreditCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public TransactionCreditCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public String getToken() {
        return token;
    }

    public TransactionCreditCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public TransactionCreditCardRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("creditCard").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toQueryString() {
        return toQueryString("creditCard");
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("cardholderName", cardholderName)
            .addElement("cvv", cvv)
            .addElement("number", number)
            .addElement("expirationDate", expirationDate)
            .addElement("expirationMonth", expirationMonth)
            .addElement("expirationYear", expirationYear)
            .addElement("token", token);
    }
}
