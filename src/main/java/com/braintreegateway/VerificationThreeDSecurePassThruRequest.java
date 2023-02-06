package com.braintreegateway;

public class VerificationThreeDSecurePassThruRequest extends ThreeDSecurePassThruRequest {
    private CreditCardVerificationRequest parent;

    public VerificationThreeDSecurePassThruRequest(CreditCardVerificationRequest parent) {
        this.parent = parent;
    }

    public CreditCardVerificationRequest done() {
        return parent;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest cavv(String cavv) {
        super.cavv(cavv);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest dsTransactionId(String dsTransactionId) {
        super.dsTransactionId(dsTransactionId);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        super.eciFlag(eciFlag);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        super.threeDSecureVersion(threeDSecureVersion);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest xid(String xid) {
        super.xid(xid);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        super.authenticationResponse(authenticationResponse);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        super.directoryResponse(directoryResponse);
        return this;
    }

    @Override
    public VerificationThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        super.cavvAlgorithm(cavvAlgorithm);
        return this;
    }
}
