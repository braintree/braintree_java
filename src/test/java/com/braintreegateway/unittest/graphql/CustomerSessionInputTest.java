package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.CustomerSessionInput;
import com.braintreegateway.graphql.inputs.PhoneInput;

class CustomerSessionInputTest {
  @Test
  void testToGraphQLVariables() {
    PhoneInput phoneInput = PhoneInput.builder().build();
    CustomerSessionInput input =
        CustomerSessionInput.builder()
            .deviceFingerprintId("device-fingerprint-id")
            .email("nobody@nowehwere.com")
            .phone(phoneInput)
            .paypalAppInstalled(true)
            .venmoAppInstalled(false)
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("device-fingerprint-id", map.get("deviceFingerprintId"));
    assertEquals("nobody@nowehwere.com", map.get("email"));
    assertEquals(phoneInput.toGraphQLVariables(), map.get("phone"));
    assertEquals(true, map.get("paypalAppInstalled"));
    assertEquals(false, map.get("venmoAppInstalled"));
  }
}
