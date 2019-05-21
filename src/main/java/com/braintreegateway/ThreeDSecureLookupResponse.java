package com.braintreegateway;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.jr.ob.JSON;
import com.braintreegateway.exceptions.UnexpectedException;

public class ThreeDSecureLookupResponse {
    private ThreeDSecureLookup lookup;
    private PaymentMethodNonce paymentMethod;
    private String payloadString;

    public ThreeDSecureLookupResponse(String lookupResponse) {
        Map<String, Object> jsonResponse = null;

        try {
            jsonResponse = JSON.std.mapFrom(lookupResponse);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
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
