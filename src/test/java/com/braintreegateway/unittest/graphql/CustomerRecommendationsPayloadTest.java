package com.braintreegateway.unittest.graphql;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

// TODO: Test based on actual object and not json string
public class CustomerRecommendationsPayloadTest {

    private String readResourceAsString(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertNotNull(is, "Test JSON file not found");
            java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";        }
    }

    @Test
    public void testCustomerRecommendationsPayload() throws IOException {
        String json = readResourceAsString("/unittest/customer_session/customer_recommendations_successful_response.json");

        // Very basic parsing for this specific structure
        assertTrue(json.contains("\"isInPayPalNetwork\": true"));
        assertTrue(json.contains("\"paymentOption\": \"PAYPAL\""));
        assertTrue(json.contains("\"recommendedPriority\": 1"));
        assertTrue(json.contains("\"paymentOption\": \"VENMO\""));
        assertTrue(json.contains("\"recommendedPriority\": 2"));
    }
}