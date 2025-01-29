package com.braintreegateway.unittest.graphql;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.RecommendedPaymentOption;
import com.braintreegateway.graphql.types.CustomerRecommendationsPayload;
import com.braintreegateway.graphql.types.PaymentOptions;
import com.braintreegateway.graphql.unions.CustomerRecommendations;

public class CustomerRecommendationsPayloadTest {

  @Test
  public void testCustomerRecommendationsPayload() throws IOException {

    CustomerRecommendations customerRecommendations = new CustomerRecommendations(
        Arrays.asList(new PaymentOptions(RecommendedPaymentOption.PAYPAL, 1),
            new PaymentOptions(RecommendedPaymentOption.VENMO, 2)));

    CustomerRecommendationsPayload payload = new CustomerRecommendationsPayload(true, customerRecommendations);

    assertEquals(true, payload.isInPayPalNetwork());
    List<PaymentOptions> paymentRecommendations = payload.getRecommendations().getPaymentOptions();
    assertEquals(RecommendedPaymentOption.PAYPAL, paymentRecommendations.get(0).getPaymentOption());
    assertEquals(1, paymentRecommendations.get(0).getRecommendedPriority());
    assertEquals(RecommendedPaymentOption.VENMO, paymentRecommendations.get(1).getPaymentOption());
    assertEquals(2, paymentRecommendations.get(1).getRecommendedPriority());
  }
}
