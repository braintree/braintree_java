package com.braintreegateway.unittest.graphql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.Recommendations;
import com.braintreegateway.graphql.inputs.CustomerRecommendationsInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;

class CustomerRecommendationsInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = CustomerSessionInput.builder().build();
    CustomerRecommendationsInput input =
        CustomerRecommendationsInput.builder("session-id", Arrays.asList(Recommendations.PAYMENT_RECOMMENDATIONS))
            .merchantAccountId("merchant-account-id")
            .customer(customerSessionInput)
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
    assertEquals(Recommendations.PAYMENT_RECOMMENDATIONS, ((List<Recommendations>) map.get("recommendations")).get(0));
  }
}
