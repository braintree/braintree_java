package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureLookupResponse;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreeDSecureLookupResponseTest {

    @Test
    public void constructor() {
        String rawResponse = "{\n" +
                "  \"paymentMethod\": {\n" +
                "    \"type\": \"CreditCard\",\n" +
                "    \"nonce\": \"some-nonce\"\n" +
                "  },\n" +
                "  \"lookup\": {\n" +
                "    \"acsUrl\": \"https://1eafstag.cardinalcommerce.com/EAFService/jsp/v1/redirect\",\n" +
                "    \"md\": \"hj3c5nx8dzdbkf23f7\",\n" +
                "    \"termUrl\": \"https://term-url.com\",\n" +
                "    \"pareq\": \"some-pareq\",\n" +
                "    \"threeDSecureVersion\": \"1.0.2\",\n" +
                "    \"transactionId\": \"some-transaction-id\"\n" +
                "  }\n" +
                "}";

        try {
            Map<String, Object> jsonResponse = JSON.std.mapFrom(rawResponse);
            ThreeDSecureLookupResponse response = new ThreeDSecureLookupResponse(jsonResponse, rawResponse);
            assertEquals(rawResponse, response.getPayloadString());
            assertEquals("some-nonce", response.getPaymentMethod().getNonce());
            assertEquals("some-transaction-id", response.getLookup().getTransactionId());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}