package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.PayPalPayeeInput;

class PayPalPayeeInputTest {

  @Test
  void testToGraphQLVariables() {
    PayPalPayeeInput input =
        PayPalPayeeInput.builder()
            .clientId("client-id")
            .emailAddress("testEmail@paypal.com")
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("client-id", map.get("clientId"));
    assertEquals("testEmail@paypal.com", map.get("emailAddress"));
  }
}
