package com.braintreegateway;

public class CreditCardVerificationRequest extends Request {

    private CreditCardVerificationCreditCardRequest creditCardRequest;
    private CreditCardVerificationOptionsRequest optionsRequest;
    private ExternalVaultVerificationRequest externalVaultVerificationRequest;
    private String intendedTransactionSource;
    private String paymentMethodNonce;
    private RiskDataVerificationRequest riskDataVerificationRequest;
    private String threeDSecureToken;
    private String threeDSecureAuthenticationID;
    private VerificationThreeDSecurePassThruRequest verificationThreeDSecurePassThruRequest;

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

    public CreditCardVerificationRequest intendedTransactionSource(String intendedTransactionSource) {
        this.intendedTransactionSource = intendedTransactionSource;
        return this;
    }

    public CreditCardVerificationRequest paymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
        return this;
    }

    public CreditCardVerificationRequest threeDSecureAuthenticationID(String threeDSecureAuthenticationID) {
        this.threeDSecureAuthenticationID = threeDSecureAuthenticationID;
        return this;
    }

    public CreditCardVerificationRequest threeDSecureToken(String threeDSecureToken) {
        this.threeDSecureToken = threeDSecureToken;
        return this;
    }

    public VerificationThreeDSecurePassThruRequest verificationThreeDSecurePassThruRequest() {
        this.verificationThreeDSecurePassThruRequest = new VerificationThreeDSecurePassThruRequest(this);
        return verificationThreeDSecurePassThruRequest;
    }

    @Override
    public String toXML() {
        return buildRequest("verification").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("creditCard", creditCardRequest)
            .addElement("options", optionsRequest)
            .addElement("externalVault", externalVaultVerificationRequest)
            .addElement("riskData", riskDataVerificationRequest)
            .addElement("intendedTransactionSource", intendedTransactionSource)
            .addElement("paymentMethodNonce", paymentMethodNonce)
            .addElement("threeDSecureAuthenticationID", threeDSecureAuthenticationID)
            .addElement("threeDSecureToken", threeDSecureToken)
            .addElement("threeDSecurePassThru", verificationThreeDSecurePassThruRequest);
        return builder;
    }
}
