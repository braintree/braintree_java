package com.braintreegateway;

public class PaymentMethodThreeDSecurePassThruRequest extends ThreeDSecurePassThruRequest {
    private PaymentMethodRequest parent;

    public PaymentMethodThreeDSecurePassThruRequest(PaymentMethodRequest parent) {
        this.parent = parent;
    }

    public PaymentMethodRequest done() {
        return parent;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest cavv(String cavv) {
        super.cavv(cavv);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest dsTransactionId(String dsTransactionId) {
        super.dsTransactionId(dsTransactionId);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        super.eciFlag(eciFlag);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        super.threeDSecureVersion(threeDSecureVersion);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest xid(String xid) {
        super.xid(xid);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        super.authenticationResponse(authenticationResponse);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        super.directoryResponse(directoryResponse);
        return this;
    }

    @Override
    public PaymentMethodThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        super.cavvAlgorithm(cavvAlgorithm);
        return this;
    }
}
