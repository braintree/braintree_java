package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonce {
    private String publicId;
    private Boolean isLocked;
    private Boolean isConsumed;
    private PaymentMethodNonceDetails details;
    private ThreeDSecureInfo threeDSecureInfo;

    public PaymentMethodNonce(NodeWrapper node) {
        publicId = node.findString("nonce");
        isLocked = node.findBoolean("locked");
        isConsumed = node.findBoolean("consumed");

        NodeWrapper detailsNode = node.findFirst("details");
        if (detailsNode != null && !detailsNode.isBlank()) {
            details = new PaymentMethodNonceDetails(detailsNode);
        }

        NodeWrapper threeDSecureInfoNode = node.findFirst("three-d-secure-info");
        if (threeDSecureInfoNode != null && !threeDSecureInfoNode.isBlank()) {
            threeDSecureInfo = new ThreeDSecureInfo(threeDSecureInfoNode);
        }
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
}
