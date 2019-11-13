package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class ThreeDSecureLookupInfo {

    private String transStatus;
    private String transStatusReason;

    public ThreeDSecureLookupInfo(NodeWrapper node) {
        transStatus = node.findString("trans-status");
        transStatusReason = node.findString("trans-status-reason");
    }

    public ThreeDSecureLookupInfo(Map<String, Object> map) {
        transStatus = (String) map.get("transStatus");
        transStatusReason = (String) map.get("transStatusReason");
    }

    public String getTransStatus() {
        return transStatus;
    }

    public String getTransStatusReason() {
        return transStatusReason;
    }
}
