package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class OAuthAccessRevocation {
    private final String merchantId;
    private final String oauthApplicationClientId;

    public OAuthAccessRevocation(NodeWrapper node) {
        this.merchantId = node.findString("merchant-id");
        this.oauthApplicationClientId = node.findString("oauth-application-client-id");
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getOauthApplicationClientId() {
        return oauthApplicationClientId;
    }
}
