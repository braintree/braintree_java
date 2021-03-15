package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.List;

public class RiskData {

    private String decision;
    private List<String> decisionReasons;
    private Boolean deviceDataCaptured;
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

    public String getTransactionRiskScore() {
        return transactionRiskScore;
    }
}
