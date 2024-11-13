package com.braintreegateway.unittest.customersession;

import com.braintreegateway.customersession.CreateCustomerSessionInput;
import com.braintreegateway.customersession.CustomerSessionInput;
import com.braintreegateway.customersession.UpdateCustomerSessionInput;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCustomerSessionInputTest {
  @Test
  void testToGraphQLVariables() {
    CustomerSessionInput customerSessionInput = new CustomerSessionInput();
    UpdateCustomerSessionInput input =
        new UpdateCustomerSessionInput("session-id")
            .merchantAccountId("merchant-account-id")
            .customer(customerSessionInput);
    Map<String, Object> map = input.toGraphQLVariables();
    assertEquals("merchant-account-id", map.get("merchantAccountId"));
    assertEquals("session-id", map.get("sessionId"));
    assertEquals(customerSessionInput.toGraphQLVariables(), map.get("customer"));
  }
}
