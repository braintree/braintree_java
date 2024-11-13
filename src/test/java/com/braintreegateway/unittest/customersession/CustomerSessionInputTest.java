package com.braintreegateway.unittest.customersession;

import com.braintreegateway.customersession.CustomerSessionInput;
import com.braintreegateway.customersession.PhoneInput;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CustomerSessionInputTest {
    @Test
    void testToGraphQLVariables() {
        PhoneInput phoneInput = new PhoneInput();
        CustomerSessionInput input = new CustomerSessionInput()
                .deviceFingerprintId("device-fingerprint-id")
                .email("nobody@nowehwere.com")
                .phone(phoneInput)
                .paypalAppInstalled(true)
                .venmoAppInstalled(false);

        Map<String, Object> map = input.toGraphQLVariables();
        
        assertEquals("device-fingerprint-id", map.get("deviceFingerprintId"));
        assertEquals("nobody@nowehwere.com", map.get("email"));
        assertEquals(phoneInput.toGraphQLVariables(), map.get("phone"));
        assertEquals(true, map.get("paypalAppInstalled"));
        assertEquals(false, map.get("venmoAppInstalled"));
    }
}