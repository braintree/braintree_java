package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class OAuthAccessRevocation {
    private final String merchantId;

    public OAuthAccessRevocation(NodeWrapper node) {
        this.merchantId = node.findString("merchant-id");
    }

    public String getMerchantId() {
        return merchantId;
    }
}
