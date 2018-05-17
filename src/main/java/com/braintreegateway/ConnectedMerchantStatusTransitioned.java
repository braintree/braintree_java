package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class ConnectedMerchantStatusTransitioned {
    private final String merchantPublicId;
    private final String oauthApplicationClientId;
    private final String status;

    public ConnectedMerchantStatusTransitioned(NodeWrapper node) {
        this.merchantPublicId = node.findString("merchant-public-id");
        this.oauthApplicationClientId = node.findString("oauth-application-client-id");
        this.status = node.findString("status");
    }

    public String getMerchantPublicId() {
        return merchantPublicId;
    }

    public String getMerchantId() {
        return merchantPublicId;
    }

    public String getStatus() {
        return status;
    }

    public String getOAuthApplicationClientId() {
        return oauthApplicationClientId;
    }
}
