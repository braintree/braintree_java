package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class RiskData {

    private String id;
    private String decision;
    private Boolean deviceDataCaptured;
    private String fraudServiceProvider;

    public RiskData(NodeWrapper node) {
        id = node.findString("id");
        decision = node.findString("decision");
        String deviceDataCapturedString = node.findString("device-data-captured");
        deviceDataCaptured = (deviceDataCapturedString == null) ? null : Boolean.valueOf(deviceDataCapturedString);
        fraudServiceProvider = node.findString("fraud-service-provider");
    }

    public String getId() {
        return id;
    }

    public String getDecision() {
        return decision;
    }

    public Boolean getDeviceDataCaptured() {
        return deviceDataCaptured;
    }

    public String getFraudServiceProvider() {
        return fraudServiceProvider;
    }
}
