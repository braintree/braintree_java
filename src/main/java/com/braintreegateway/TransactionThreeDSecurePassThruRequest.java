package com.braintreegateway;

public class TransactionThreeDSecurePassThruRequest extends Request {
    private TransactionRequest parent;
    private String eciFlag;
    private String cavv;
    private String xid;

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

    public TransactionThreeDSecurePassThruRequest xid(String xid) {
        this.xid = xid;
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
            addElement("xid", xid);
    }
}
