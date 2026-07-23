package com.braintreegateway;

import com.braintreegateway.enums.ThreeDSecurePassThruNetwork;

public class ThreeDSecurePassThruRequest extends Request {
    private String eciFlag;
    private String cavv;
    private String threeDSecureVersion;
    private String xid;
    private String authenticationResponse;
    private String directoryResponse;
    private String cavvAlgorithm;
    private String dsTransactionId;
    private ThreeDSecurePassThruNetwork network;

    public ThreeDSecurePassThruRequest eciFlag(String eciFlag) {
        this.eciFlag = eciFlag;
        return this;
    }

    public ThreeDSecurePassThruRequest cavv(String cavv) {
        this.cavv = cavv;
        return this;
    }

    public ThreeDSecurePassThruRequest threeDSecureVersion(String threeDSecureVersion) {
        this.threeDSecureVersion = threeDSecureVersion;
        return this;
    }

    public ThreeDSecurePassThruRequest xid(String xid) {
        this.xid = xid;
        return this;
    }

    public ThreeDSecurePassThruRequest authenticationResponse(String authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
        return this;
    }

    public ThreeDSecurePassThruRequest directoryResponse(String directoryResponse) {
        this.directoryResponse = directoryResponse;
        return this;
    }

    public ThreeDSecurePassThruRequest cavvAlgorithm(String cavvAlgorithm) {
        this.cavvAlgorithm = cavvAlgorithm;
        return this;
    }

    public ThreeDSecurePassThruRequest dsTransactionId(String dsTransactionId) {
        this.dsTransactionId = dsTransactionId;
        return this;
    }

    public ThreeDSecurePassThruRequest network(ThreeDSecurePassThruNetwork network) {
        this.network = network;
        return this;
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
        return new RequestBuilder(root)
            .addElement("eciFlag", eciFlag)
            .addElement("cavv", cavv)
            .addElement("threeDSecureVersion", threeDSecureVersion)
            .addElement("xid", xid)
            .addElement("authenticationResponse", authenticationResponse)
            .addElement("directoryResponse", directoryResponse)
            .addElement("cavvAlgorithm", cavvAlgorithm)
            .addElement("dsTransactionId", dsTransactionId)
            .addElement("network", network);
    }
}
