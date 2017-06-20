package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonce {
    private Boolean isDefault;
    private String publicId;
    private Boolean isLocked;
    private Boolean isConsumed;
    private PaymentMethodNonceDetails details;
    private ThreeDSecureInfo threeDSecureInfo;
    private String type;

    public PaymentMethodNonce(NodeWrapper node) {
        isDefault = node.findBoolean("default");
        publicId = node.findString("nonce");
        isLocked = node.findBoolean("locked");
        isConsumed = node.findBoolean("consumed");
        type = node.findString("type");

        NodeWrapper detailsNode = node.findFirst("details");
        if (detailsNode != null && !detailsNode.isBlank()) {
            details = new PaymentMethodNonceDetails(detailsNode);
        }

        NodeWrapper threeDSecureInfoNode = node.findFirst("three-d-secure-info");
        if (threeDSecureInfoNode != null && !threeDSecureInfoNode.isBlank()) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoNode);
        }
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getNonce() {
        return getPublicId();
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public Boolean isConsumed() {
        return isConsumed;
    }

    public PaymentMethodNonceDetails getDetails() {
        return details;
    }

    public ThreeDSecureInfo getThreeDSecureInfo() {
        return threeDSecureInfo;
    }

    public String getType() {
        return type;
    }
}
