package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class ConnectedMerchantPayPalStatusChanged {
    private final String merchantPublicId;
    private final String oauthApplicationClientId;
    private final String action;

    public ConnectedMerchantPayPalStatusChanged(NodeWrapper node) {
        this.merchantPublicId = node.findString("merchant-public-id");
        this.oauthApplicationClientId = node.findString("oauth-application-client-id");
        this.action = node.findString("action");
    }

    public String getMerchantPublicId() {
        return merchantPublicId;
    }

    public String getMerchantId() {
        return merchantPublicId;
    }

    public String getAction() {
        return action;
    }

    public String getOAuthApplicationClientId() {
        return oauthApplicationClientId;
    }
}
