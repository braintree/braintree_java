package com.braintreegateway.unittest.customersession;

import com.braintreegateway.Result;
import com.braintreegateway.customersession.*;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.GraphQLClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerInsightsPayloadTest {

    @Test
    public void testCustomerInsightsPayload() throws IOException {
        Map<String, Object> payloadResponse = TestHelper.readResponseFromJsonResource("unittest/customer_session/customer_insights_successful_response.json");

        CustomerInsightsPayload payload = new CustomerInsightsPayload((Map<String, Object>) payloadResponse.get("data"));

        assertEquals(true, payload.isInPayPalNetwork());
        List<PaymentRecommendation> paymentRecommendations = payload.getInsights().getPaymentRecommendations();
        assertEquals(InsightPaymentOption.PAYPAL, paymentRecommendations.get(0).getPaymentOption());
        assertEquals(1, paymentRecommendations.get(0).getRecommendedPriority());
        assertEquals(InsightPaymentOption.VENMO, paymentRecommendations.get(1).getPaymentOption());
        assertEquals(2, paymentRecommendations.get(1).getRecommendedPriority());
    }
}