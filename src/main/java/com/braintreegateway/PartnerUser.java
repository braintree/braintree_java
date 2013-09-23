package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class PartnerUser {
    private final String merchantPublicId;
    private final String publicKey;
    private final String privateKey;
    private final String partnerUserId;

    public PartnerUser(NodeWrapper node) {
        this.merchantPublicId = node.findString("merchant-public-id");
        this.publicKey = node.findString("public-key");
        this.privateKey = node.findString("private-key");
        this.partnerUserId = node.findString("partner-user-id");
    }

    public String getMerchantPublicId() {
        return merchantPublicId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }
}

