package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests around {@link CreditCardVerification CreditCardVerifications}.
 */
public class ExternalVaultVerificationRequest extends Request {
    private CreditCardVerificationRequest parent;
    private ExternalVault.Status status;
    private String previousNetworkTransactionId;
    private String previousMastercardTransactionLinkId;

    public ExternalVaultVerificationRequest(CreditCardVerificationRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationRequest done() {
        return parent;
    }

    public ExternalVaultVerificationRequest willVault() {
        this.status = ExternalVault.Status.WILL_VAULT;
        return this;
    }

    public ExternalVaultVerificationRequest vaulted() {
        this.status = ExternalVault.Status.VAULTED;
        return this;
    }

    public ExternalVaultVerificationRequest status(ExternalVault.Status status) {
        this.status = status;
        return this;
    }

    public ExternalVaultVerificationRequest previousNetworkTransactionId(String previousNetworkTransactionId) {
        this.previousNetworkTransactionId = previousNetworkTransactionId;
        return this;
    }

    public ExternalVaultVerificationRequest previousMastercardTransactionLinkId(String previousMastercardTransactionLinkId) {
        this.previousMastercardTransactionLinkId = previousMastercardTransactionLinkId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("externalVault").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("status", status)
            .addElement("previousNetworkTransactionId", previousNetworkTransactionId)
            .addElement("previousMastercardTransactionLinkId", previousMastercardTransactionLinkId);
    }
}
