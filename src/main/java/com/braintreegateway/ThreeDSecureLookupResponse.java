package com.braintreegateway;

import java.util.Map;

public class ThreeDSecureLookupResponse {
    private ThreeDSecureLookup lookup;
    private PaymentMethodNonce paymentMethod;
    private String payloadString;

    public ThreeDSecureLookupResponse(Map<String, Object> jsonResponse, String lookupResponse) {
        payloadString = lookupResponse;
        lookup = new ThreeDSecureLookup((Map) jsonResponse.get("lookup"));
        paymentMethod = new PaymentMethodNonce((Map) jsonResponse.get("paymentMethod"));
    }

    public ThreeDSecureLookup getLookup() {
        return lookup;
    }

    public PaymentMethodNonce getPaymentMethod() {
        return paymentMethod;
    }

    public String getPayloadString() {
        return payloadString;
    }
}
