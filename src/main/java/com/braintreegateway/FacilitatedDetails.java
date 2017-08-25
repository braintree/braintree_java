package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class FacilitatedDetails {
    private final String merchantId;
    private final String merchantName;
    private final String paymentMethodNonce;

    public FacilitatedDetails(NodeWrapper node) {
        merchantId = node.findString("merchant-id");
        merchantName = node.findString("merchant-name");
        paymentMethodNonce = node.findString("payment-method-nonce");
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getPaymentMethodNonce() {
        return paymentMethodNonce;
    }
}
