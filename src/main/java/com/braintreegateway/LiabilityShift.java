package com.braintreegateway;

import java.util.List;

import com.braintreegateway.util.NodeWrapper;

public class LiabilityShift {

    private String responsibleParty;
    private List<String> conditions;

    public LiabilityShift(NodeWrapper node) {
      responsibleParty = node.findString("responsible-party");
      conditions = node.findAllStrings("conditions");
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public List<String> getConditions() {
        return conditions;
    }
}
