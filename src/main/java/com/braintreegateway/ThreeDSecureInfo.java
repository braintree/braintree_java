package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Map;

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
    private String acsTransactionId;
    private String paresStatus;
    private String threeDSecureServerTransactionId;
    private ThreeDSecureLookupInfo threeDSecureLookupInfo;
    private ThreeDSecureAuthenticateInfo threeDSecureAuthenticateInfo;

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
        acsTransactionId = node.findString("acs-transaction-id");
        paresStatus = node.findString("pares-status");
        threeDSecureServerTransactionId = node.findString("three-d-secure-server-transaction-id");

        NodeWrapper threeDSecureLookupInfoNode = node.findFirst("lookup");
        if (threeDSecureLookupInfoNode != null && !threeDSecureLookupInfoNode.isBlank()) {
            threeDSecureLookupInfo = new ThreeDSecureLookupInfo(threeDSecureLookupInfoNode);
        }
        NodeWrapper threeDSecureAuthenticateInfoNode = node.findFirst("authenticate");
        if (threeDSecureAuthenticateInfoNode != null && !threeDSecureAuthenticateInfoNode.isBlank()) {
            threeDSecureAuthenticateInfo = new ThreeDSecureAuthenticateInfo(threeDSecureAuthenticateInfoNode);
        }
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
        acsTransactionId = (String) map.get("acsTransactionId");
        paresStatus = (String) map.get("paresStatus");
        threeDSecureServerTransactionId = (String) map.get("threeDSecureServerTransactionId");

        Map<String, Object> threeDSecureLookupInfoMap = (Map) map.get("lookup");
        if (threeDSecureLookupInfoMap != null) {
            threeDSecureLookupInfo = new ThreeDSecureLookupInfo(threeDSecureLookupInfoMap);
        }
        Map<String, Object> threeDSecureAuthenticateInfoMap = (Map) map.get("authenticate");
        if (threeDSecureAuthenticateInfoMap != null) {
            threeDSecureAuthenticateInfo = new ThreeDSecureAuthenticateInfo(threeDSecureAuthenticateInfoMap);
        }
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

    public String getAcsTransactionId() {
        return acsTransactionId;
    }

    public String getParesStatus() {
        return paresStatus;
    }

    public String getThreeDSecureServerTransactionId() {
        return threeDSecureServerTransactionId;
    }

    public ThreeDSecureLookupInfo getThreeDSecureLookupInfo() {
        return threeDSecureLookupInfo;
    }

    public ThreeDSecureAuthenticateInfo getThreeDSecureAuthenticateInfo() {
        return threeDSecureAuthenticateInfo;
    }
}
