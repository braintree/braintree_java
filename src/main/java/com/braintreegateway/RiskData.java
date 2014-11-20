package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class RiskData {

    private String id;
    private String decision;

    public RiskData(NodeWrapper node) {
        id = node.findString("id");
        decision = node.findString("decision");
    }

    public String getId() {
        return id;
    }

    public String getDecision() {
        return decision;
    }
}
