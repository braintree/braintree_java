package com.braintreegateway.unittest.customersession;

import com.braintreegateway.customersession.CreateCustomerSessionInput;
import com.braintreegateway.customersession.CustomerSessionInput;
import com.braintreegateway.customersession.PhoneInput;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCustomerSessionInputTest {
    @Test
    void testToGraphQLVariables() {
        CustomerSessionInput customerSessionInput = new CustomerSessionInput();
        CreateCustomerSessionInput input = new CreateCustomerSessionInput()
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