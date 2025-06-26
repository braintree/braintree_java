package com.braintreegateway.unittest.graphql;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CustomerRecommendationsPayloadTest {

    private String readResourceAsString(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            assertNotNull(is, "Test JSON file not found");
            java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";        }
    }

    @Test
    public void testCustomerRecommendationsPayload() throws IOException {
        Map<String, Object> successResponse = TestHelper.readResponseFromJsonResource(
                        "unittest/customer_session/customer_recommendations_successful_response.json");

        CustomerRecommendationsPayload payload = new CustomerRecommendationsPayload((Map<String, Object> )successResponse.get("data"));

        assertEquals("a-customer-session-id", payload.getSessionId());
        assertTrue(payload.isInPayPalNetwork());

        List<PaymentOptions> paymentOptions = payload.getRecommendations().getPaymentOptions();
        assertNotNull(paymentOptions);
        assertEquals(2, paymentOptions.size());

        PaymentOptions paypalOption = paymentOptions.stream()
            .filter(po -> po.getPaymentOption() == RecommendedPaymentOption.PAYPAL)
            .findFirst()
            .orElse(null);
        assertNotNull(paypalOption);
        assertEquals(1, paypalOption.getRecommendedPriority());

        PaymentOptions venmoOption = paymentOptions.stream()
            .filter(po -> po.getPaymentOption() == RecommendedPaymentOption.VENMO)
            .findFirst()
            .orElse(null);
        assertNotNull(venmoOption);
        assertEquals(2, venmoOption.getRecommendedPriority());
    }
}