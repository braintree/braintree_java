package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.BillingAddressInput;

class BillingAddressInputTest {
    @Test
    void testToGraphQLVariables() {
        BillingAddressInput input = BillingAddressInput.builder()
            .countryCodeAlpha2("US")
            .streetAddress("123 Main St")
            .extendedAddress("Apt 4")
            .locality("San Francisco")
            .region("CA")
            .postalCode("94103")
            .build();

        Map<String, Object> map = input.toGraphQLVariables();

        assertEquals("US", map.get("countryCode"));
        assertEquals("123 Main St", map.get("streetAddress"));
        assertEquals("Apt 4", map.get("extendedAddress"));
        assertEquals("San Francisco", map.get("locality"));
        assertEquals("CA", map.get("region"));
        assertEquals("94103", map.get("postalCode"));
    }
}
