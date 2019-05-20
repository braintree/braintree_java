package com.braintreegateway;

import java.util.Map;

public class ThreeDSecureLookup {
    private final String acsUrl;
    private final String threeDSecureVersion;
    private final String transactionId;

    public ThreeDSecureLookup(Map<String, String> lookupResponse) {
        acsUrl = lookupResponse.get("acsUrl");
        threeDSecureVersion = lookupResponse.get("threeDSecureVersion");
        transactionId = lookupResponse.get("transactionId");
    }

    public String getAcsUrl() {
        return acsUrl;
    }

    public String getThreeDSecureVersion() {
        return threeDSecureVersion;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
