package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class PaymentMethodNonce {
    private String publicId;
    private Boolean isLocked;
    private Boolean isConsumed;

    public PaymentMethodNonce(NodeWrapper node) {
        publicId = node.findString("nonce");
        isLocked = node.findBoolean("locked");
        isConsumed = node.findBoolean("consumed");
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
}
