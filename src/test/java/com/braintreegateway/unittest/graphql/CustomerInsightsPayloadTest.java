package com.braintreegateway.unittest.graphql;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.InsightPaymentOption;
import com.braintreegateway.graphql.types.CustomerInsightsPayload;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.testhelpers.TestHelper;

public class CustomerInsightsPayloadTest {

  @Test
  public void testCustomerInsightsPayload() throws IOException {
    Map<String, Object> payloadResponse =
        TestHelper.readResponseFromJsonResource(
            "unittest/customer_session/customer_insights_successful_response.json");

    CustomerInsightsPayload payload =
        new CustomerInsightsPayload((Map<String, Object>) payloadResponse.get("data"));

    assertEquals(true, payload.isInPayPalNetwork());
    List<PaymentRecommendation> paymentRecommendations =
        payload.getInsights().getPaymentRecommendations();
    assertEquals(InsightPaymentOption.PAYPAL, paymentRecommendations.get(0).getPaymentOption());
    assertEquals(1, paymentRecommendations.get(0).getRecommendedPriority());
    assertEquals(InsightPaymentOption.VENMO, paymentRecommendations.get(1).getPaymentOption());
    assertEquals(2, paymentRecommendations.get(1).getRecommendedPriority());
  }
}
