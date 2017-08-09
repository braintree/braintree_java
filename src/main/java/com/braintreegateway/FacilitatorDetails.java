package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class FacilitatorDetails {
    private final String oauthApplicationClientId;
    private final String oauthApplicationName;
    private final String sourcePaymentMethodToken;

    public FacilitatorDetails(NodeWrapper node) {
        oauthApplicationClientId = node.findString("oauth-application-client-id");
        oauthApplicationName = node.findString("oauth-application-name");
        sourcePaymentMethodToken = node.findString("source-payment-method-token");
    }

    public String getOauthApplicationClientId() {
        return oauthApplicationClientId;
    }

    public String getOauthApplicationName() {
        return oauthApplicationName;
    }

    public String getSourcePaymentMethodToken() {
        return sourcePaymentMethodToken;
    }
}
