package com.braintreegateway;

public class CreditCardVerificationCreditCardRequest extends Request {
    private CreditCardVerificationRequest parent;
    private String cardholderName;
    private String cvv;
    private String expirationDate;
    private String expirationMonth;
    private String expirationYear;
    private String number;
    private CreditCardVerificationBillingAddressRequest billingAddress;

    public CreditCardVerificationCreditCardRequest(CreditCardVerificationRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationRequest done() {
        return parent;
    }

    public CreditCardVerificationCreditCardRequest cardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        return this;
    }

    public CreditCardVerificationCreditCardRequest cvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public CreditCardVerificationCreditCardRequest expirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public CreditCardVerificationCreditCardRequest expirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
        return this;
    }

    public CreditCardVerificationCreditCardRequest expirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
        return this;
    }

    public CreditCardVerificationCreditCardRequest number(String number) {
        this.number = number;
        return this;
    }

    public CreditCardVerificationBillingAddressRequest billingAddress() {
        billingAddress = new CreditCardVerificationBillingAddressRequest(this);
        return billingAddress;
    }

    @Override
    public String toXML() {
        return buildRequest("creditCard").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("cardholderName", cardholderName)
            .addElement("cvv", cvv)
            .addElement("number", number)
            .addElement("expirationDate", expirationDate)
            .addElement("expirationMonth", expirationMonth)
            .addElement("expirationYear", expirationYear)
            .addElement("billingAddress", billingAddress);
    }
}
