package com.braintreegateway;

public class TransactionThreeDSecurePassThruRequest extends Request {
    private TransactionRequest parent;
    private String eciFlag;
    private String cavv;
    private String threeDSecureVersion;
    private String xid;
    private String authenticationResponse;
    private String directoryResponse;
    private String cavvAlgorithm;

    public TransactionThreeDSecurePassThruRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public TransactionThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        this.eciFlag = eciFlag;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest cavv(String cavv) {
        this.cavv = cavv;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        this.threeDSecureVersion = threeDSecureVersion;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest xid(String xid) {
        this.xid = xid;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        this.directoryResponse = directoryResponse;
        return this;
    }

    public TransactionThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        this.cavvAlgorithm = cavvAlgorithm;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("threeDSecurePassThru").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("threeDSecurePassThru");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
            addElement("eciFlag", eciFlag).
            addElement("cavv", cavv).
            addElement("threeDSecureVersion", threeDSecureVersion).
            addElement("xid", xid).
            addElement("authenticationResponse", authenticationResponse).
            addElement("directoryResponse", directoryResponse).
            addElement("cavvAlgorithm", cavvAlgorithm);
    }
}
