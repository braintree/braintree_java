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
            .hashedEmail("hashed@paypal.com")
            .hashedPhoneNumber("000-000-0000")
            .phone(phoneInput)
            .paypalAppInstalled(true)
            .venmoAppInstalled(false)
            .userAgent("Mozilla")
            .build();

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("device-fingerprint-id", map.get("deviceFingerprintId"));
    assertEquals("nobody@nowehwere.com", map.get("email"));
    assertEquals(phoneInput.toGraphQLVariables(), map.get("phone"));
    assertEquals(true, map.get("paypalAppInstalled")); 
    assertEquals("hashed@paypal.com", map.get("hashedEmail")); 
    assertEquals("000-000-0000", map.get("hashedPhoneNumber"));
    assertEquals(false, map.get("venmoAppInstalled"));
    assertEquals("Mozilla", map.get("userAgent"));
  }
}
