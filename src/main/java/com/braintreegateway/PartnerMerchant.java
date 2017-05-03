package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class PartnerMerchant {
    private final String merchantPublicId;
    private final String publicKey;
    private final String privateKey;
    private final String partnerMerchantId;
    private final String clientSideEncryptionKey;

    public PartnerMerchant(NodeWrapper node) {
        this.merchantPublicId = node.findString("merchant-public-id");
        this.publicKey = node.findString("public-key");
        this.privateKey = node.findString("private-key");
        this.partnerMerchantId = node.findString("partner-merchant-id");
        this.clientSideEncryptionKey = node.findString("client-side-encryption-key");
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

    public String getPartnerMerchantId() {
        return partnerMerchantId;
    }

    public String getClientSideEncryptionKey() {
        return clientSideEncryptionKey;
    }
}
