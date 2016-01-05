package com.braintreegateway;

public class CreditCardVerificationRequest extends Request {

    private CreditCardVerificationCreditCardRequest creditCardRequest;

    public CreditCardVerificationRequest() {
    }

    public CreditCardVerificationCreditCardRequest creditCard() {
        creditCardRequest = new CreditCardVerificationCreditCardRequest(this);
        return creditCardRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("verification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("creditCard", creditCardRequest);
        return builder;
    }
}
