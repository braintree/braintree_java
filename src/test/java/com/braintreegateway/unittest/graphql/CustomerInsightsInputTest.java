package com.braintreegateway.unittest.graphql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.enums.Insights;
import com.braintreegateway.graphql.inputs.CustomerInsightsInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;

class CustomerInsightsInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = new CustomerSessionInput();
    CustomerInsightsInput input =
        new CustomerInsightsInput("session-id", Arrays.asList(Insights.PAYMENT_INSIGHTS))
            .merchantAccountId("merchant-account-id")
            .customer(customerSessionInput);

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
    assertEquals(Insights.PAYMENT_INSIGHTS, ((List<Insights>) map.get("insights")).get(0));
  }
}
