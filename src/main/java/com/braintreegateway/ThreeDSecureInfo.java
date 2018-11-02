package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class ThreeDSecureInfo {

    private boolean liabilityShifted;
    private boolean liabilityShiftPossible;
    private String enrolled;
    private String status;
    private String cavv;
    private String eciFlag;
    private String xid;
    private String threeDSecureVersion;

    public ThreeDSecureInfo(NodeWrapper node) {
        liabilityShifted = node.findBoolean("liability-shifted");
        liabilityShiftPossible = node.findBoolean("liability-shift-possible");
        enrolled = node.findString("enrolled");
        status = node.findString("status");
        cavv = node.findString("cavv");
        eciFlag = node.findString("eci-flag");
        xid = node.findString("xid");
        threeDSecureVersion = node.findString("three-d-secure-version");
    }

    public String getStatus() {
        return status;
    }

    public String getEnrolled() {
        return enrolled;
    }

    public boolean isLiabilityShifted() {
        return liabilityShifted;
    }

    public boolean isLiabilityShiftPossible() {
        return liabilityShiftPossible;
    }

    public String getCAVV() {
        return cavv;
    }

    public String getECIFlag() {
        return eciFlag;
    }

    public String getXID() {
        return xid;
    }

    public String getThreeDSecureVersion() {
        return threeDSecureVersion;
    }
}
