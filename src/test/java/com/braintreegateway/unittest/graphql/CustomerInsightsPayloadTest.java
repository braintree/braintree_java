package com.braintreegateway.unittest.graphql;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.InsightPaymentOption;
import com.braintreegateway.graphql.types.CustomerInsightsPayload;
import com.braintreegateway.graphql.types.PaymentRecommendation;
import com.braintreegateway.graphql.unions.CustomerInsights;

public class CustomerInsightsPayloadTest {

  @Test
  public void testCustomerInsightsPayload() throws IOException {

    CustomerInsights customerInsights = new CustomerInsights(
        Arrays.asList(new PaymentRecommendation(InsightPaymentOption.PAYPAL, 1),
            new PaymentRecommendation(InsightPaymentOption.VENMO, 2)));

    CustomerInsightsPayload payload = new CustomerInsightsPayload(true, customerInsights);

    assertEquals(true, payload.isInPayPalNetwork());
    List<PaymentRecommendation> paymentRecommendations = payload.getInsights().getPaymentRecommendations();
    assertEquals(InsightPaymentOption.PAYPAL, paymentRecommendations.get(0).getPaymentOption());
    assertEquals(1, paymentRecommendations.get(0).getRecommendedPriority());
    assertEquals(InsightPaymentOption.VENMO, paymentRecommendations.get(1).getPaymentOption());
    assertEquals(2, paymentRecommendations.get(1).getRecommendedPriority());
  }
}
