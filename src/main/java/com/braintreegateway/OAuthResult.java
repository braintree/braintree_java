package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class OAuthResult {

    private Boolean result;

    public OAuthResult(NodeWrapper node) {
        result = node.findBoolean("success");
    }

    public Boolean getResult() {
        return result;
    }
}
