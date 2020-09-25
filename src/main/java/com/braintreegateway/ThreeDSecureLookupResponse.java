package com.braintreegateway;

import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.Map;

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

        if (jsonResponse.get("error") != null) {
          Map<String, Object> error = (Map) jsonResponse.get("error");
          throw new UnexpectedException((String) error.get("message"));
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
