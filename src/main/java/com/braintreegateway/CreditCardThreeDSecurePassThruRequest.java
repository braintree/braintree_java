package com.braintreegateway;

public class CreditCardThreeDSecurePassThruRequest extends ThreeDSecurePassThruRequest {
    private CreditCardRequest parent;

    public CreditCardThreeDSecurePassThruRequest(CreditCardRequest parent) {
        this.parent = parent;
    }

    public CreditCardRequest done() {
        return parent;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest cavv(String cavv) {
        super.cavv(cavv);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest dsTransactionId(String dsTransactionId) {
        super.dsTransactionId(dsTransactionId);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        super.eciFlag(eciFlag);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        super.threeDSecureVersion(threeDSecureVersion);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest xid(String xid) {
        super.xid(xid);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        super.authenticationResponse(authenticationResponse);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        super.directoryResponse(directoryResponse);
        return this;
    }

    @Override
    public CreditCardThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        super.cavvAlgorithm(cavvAlgorithm);
        return this;
    }
}
