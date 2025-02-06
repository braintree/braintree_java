package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.UpdateCustomerSessionInput;

class UpdateCustomerSessionInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = CustomerSessionInput.builder().build();
    UpdateCustomerSessionInput input = UpdateCustomerSessionInput.builder("session-id")
        .merchantAccountId("merchant-account-id")
        .customer(customerSessionInput)
        .build();
    Map<String, Object> map = input.toGraphQLVariables();
    assertEquals("merchant-account-id", map.get("merchantAccountId"));
    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
  }
}
