package com.braintreegateway;

public class TransactionThreeDSecurePassThruRequest extends ThreeDSecurePassThruRequest {
    private TransactionRequest parent;

    public TransactionThreeDSecurePassThruRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest cavv(String cavv) {
        super.cavv(cavv);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest dsTransactionId(String dsTransactionId) {
        super.dsTransactionId(dsTransactionId);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        super.eciFlag(eciFlag);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        super.threeDSecureVersion(threeDSecureVersion);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest xid(String xid) {
        super.xid(xid);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        super.authenticationResponse(authenticationResponse);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        super.directoryResponse(directoryResponse);
        return this;
    }

    @Override
    public TransactionThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        super.cavvAlgorithm(cavvAlgorithm);
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }
}
