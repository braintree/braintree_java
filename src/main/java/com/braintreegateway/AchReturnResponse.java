package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class AchReturnResponse {

    private String createdAt;
    private String reasonCode;

    public AchReturnResponse(NodeWrapper node) {
        createdAt = node.findString("created-at");
        reasonCode = node.findString("reason-code");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}
