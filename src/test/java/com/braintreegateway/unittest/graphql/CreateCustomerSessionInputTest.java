package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.CreateCustomerSessionInput;
import com.braintreegateway.graphql.inputs.CustomerSessionInput;

class CreateCustomerSessionInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = new CustomerSessionInput();
    CreateCustomerSessionInput input =
        new CreateCustomerSessionInput()
            .merchantAccountId("merchant-account-id")
            .sessionId("session-id")
            .customer(customerSessionInput)
            .domain("a-domain");

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("merchant-account-id", map.get("merchantAccountId"));
    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
    assertEquals("a-domain", map.get("domain"));
  }
}
