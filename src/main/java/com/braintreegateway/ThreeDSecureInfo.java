package com.braintreegateway;

import java.util.Map;
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
    private String dsTransactionId;
    private String threeDSecureAuthenticationId;

    public ThreeDSecureInfo(NodeWrapper node) {
        liabilityShifted = node.findBoolean("liability-shifted");
        liabilityShiftPossible = node.findBoolean("liability-shift-possible");
        enrolled = node.findString("enrolled");
        status = node.findString("status");
        cavv = node.findString("cavv");
        eciFlag = node.findString("eci-flag");
        xid = node.findString("xid");
        threeDSecureVersion = node.findString("three-d-secure-version");
        dsTransactionId = node.findString("ds-transaction-id");
        threeDSecureAuthenticationId = node.findString("three-d-secure-authentication-id");
    }

    public ThreeDSecureInfo(Map<String, Object> map) {
        liabilityShifted = (Boolean) map.get("liabilityShifted");
        liabilityShiftPossible = (Boolean) map.get("liabilityShiftPossible");
        enrolled = (String) map.get("enrolled");
        status = (String) map.get("status");
        cavv = (String) map.get("cavv");
        eciFlag = (String) map.get("eciFlag");
        xid = (String) map.get("xid");
        threeDSecureVersion = (String) map.get("threeDSecureVersion");
        dsTransactionId = (String) map.get("dsTransactionId");
        threeDSecureAuthenticationId = (String) map.get("threeDSecureAuthenticationId");
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

    public String getDsTransactionId() {
        return dsTransactionId;
    }

    public String getThreeDSecureAuthenticationId() {
        return threeDSecureAuthenticationId;
    }
}
