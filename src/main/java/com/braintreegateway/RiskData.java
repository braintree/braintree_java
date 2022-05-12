package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.List;
import java.util.ArrayList;

public class RiskData {

    private Boolean deviceDataCaptured;
    private LiabilityShift liabilityShift;
    private List<String> decisionReasons;
    private String decision;
    private String fraudServiceProvider;
    private String id;
    private String transactionRiskScore;

    public RiskData(NodeWrapper node) {
        id = node.findString("id");
        decision = node.findString("decision");
        String deviceDataCapturedString = node.findString("device-data-captured");
        deviceDataCaptured = (deviceDataCapturedString == null) ? null : Boolean.valueOf(deviceDataCapturedString);
        fraudServiceProvider = node.findString("fraud-service-provider");
        transactionRiskScore = node.findString("transaction-risk-score");

        decisionReasons = node.findAllStrings("decision-reasons");

        NodeWrapper liabilityShiftNode = node.findFirst("liability-shift");
        if (liabilityShiftNode != null && !liabilityShiftNode.isBlank()) {
            liabilityShift = new LiabilityShift(liabilityShiftNode);
        }
    }

    public String getId() {
        return id;
    }

    public String getDecision() {
        return decision;
    }

    public List<String> getDecisionReasons() {
        return decisionReasons;
    }

    public Boolean getDeviceDataCaptured() {
        return deviceDataCaptured;
    }

    public String getFraudServiceProvider() {
        return fraudServiceProvider;
    }

    public LiabilityShift getLiabilityShift() {
        return liabilityShift;
    }

    public String getTransactionRiskScore() {
        return transactionRiskScore;
    }
}
