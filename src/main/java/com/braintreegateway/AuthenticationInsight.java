package com.braintreegateway;

import java.util.Map;
import com.braintreegateway.util.NodeWrapper;

public class AuthenticationInsight {

    private String regulationEnvironment;
    private String message;

    public AuthenticationInsight(NodeWrapper node) {
        regulationEnvironment = node.findString("regulation-environment");
        message = node.findString("message");
    }

    public AuthenticationInsight(Map<String, Object> map) {
        regulationEnvironment = (String) map.get("regulationEnvironment");
        message = (String) map.get("message");
    }

    public String getRegulationEnvironment() {
        return regulationEnvironment;
    }

    public String getMessage() {
        return message;
    }
}
