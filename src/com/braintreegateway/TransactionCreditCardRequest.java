package com.braintreegateway;

import com.braintreegateway.util.QueryString;

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
        StringBuilder builder = new StringBuilder();
        builder.append("<creditCard>");
        builder.append(buildXMLElement("cardholderName", cardholderName));
        builder.append(buildXMLElement("cvv", cvv));
        builder.append(buildXMLElement("number", number));
        builder.append(buildXMLElement("expirationDate", expirationDate));
        builder.append(buildXMLElement("expirationMonth", expirationMonth));
        builder.append(buildXMLElement("expirationYear", expirationYear));
        builder.append(buildXMLElement("token", token));
        builder.append("</creditCard>");
        return builder.toString();
    }

    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "cardholder_name"), cardholderName).
            append(parentBracketChildString(root, "cvv"), cvv).
            append(parentBracketChildString(root, "number"), number).
            append(parentBracketChildString(root, "expiration_date"), expirationDate).
            append(parentBracketChildString(root, "expiration_month"), expirationMonth).
            append(parentBracketChildString(root, "expiration_year"), expirationYear).
            append(parentBracketChildString(root, "token"), token).
            toString();
    }

    public String toQueryString() {
        return toQueryString("credit_card");
    }
}
