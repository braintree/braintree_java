package com.braintreegateway.unittest.customersession;

import com.braintreegateway.customersession.PhoneInput;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PhoneInputTest {

  @Test
  void testToGraphQLVariables() {
    PhoneInput input =
        new PhoneInput().countryPhoneCode("1").phoneNumber("5555555555").extensionNumber("5555");

    Map<String, Object> map = input.toGraphQLVariables();

    assertEquals("1", map.get("countryPhoneCode"));
    assertEquals("5555555555", map.get("phoneNumber"));
    assertEquals("5555", map.get("extensionNumber"));
  }
}
