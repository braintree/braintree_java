package com.braintreegateway;

public class CreditCardVerificationRequest extends Request {

    private CreditCardVerificationCreditCardRequest creditCardRequest;
    private CreditCardVerificationOptionsRequest optionsRequest;

    public CreditCardVerificationRequest() {
    }

    public CreditCardVerificationCreditCardRequest creditCard() {
        creditCardRequest = new CreditCardVerificationCreditCardRequest(this);
        return creditCardRequest;
    }

    public CreditCardVerificationOptionsRequest options() {
        optionsRequest = new CreditCardVerificationOptionsRequest(this);
        return optionsRequest;
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
