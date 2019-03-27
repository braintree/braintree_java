package com.braintreegateway;

public class CreditCardVerificationRequest extends Request {

    private CreditCardVerificationCreditCardRequest creditCardRequest;
    private CreditCardVerificationOptionsRequest optionsRequest;
    private ExternalVaultVerificationRequest externalVaultVerificationRequest;
    private RiskDataVerificationRequest riskDataVerificationRequest;

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

    public ExternalVaultVerificationRequest externalVault() {
        externalVaultVerificationRequest = new ExternalVaultVerificationRequest(this);
        return externalVaultVerificationRequest;
    }

    public RiskDataVerificationRequest riskData() {
        riskDataVerificationRequest = new RiskDataVerificationRequest(this);
        return riskDataVerificationRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("verification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("creditCard", creditCardRequest).
            addElement("options", optionsRequest).
            addElement("externalVault", externalVaultVerificationRequest).
            addElement("riskData", riskDataVerificationRequest);
        return builder;
    }
}
