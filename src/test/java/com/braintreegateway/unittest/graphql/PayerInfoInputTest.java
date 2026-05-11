package com.braintreegateway.unittest.graphql;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.braintreegateway.graphql.inputs.BillingAddressInput;
import com.braintreegateway.graphql.inputs.PayerInfoInput;
import com.braintreegateway.graphql.inputs.ShippingAddressInput;

class PayerInfoInputTest {
    @Test
    void testToGraphQLVariables() {
        BillingAddressInput billingAddress = BillingAddressInput.builder()
            .countryCodeAlpha2("US")
            .streetAddress("123 Main St")
            .locality("San Francisco")
            .region("CA")
            .postalCode("94103")
            .build();

        PayerInfoInput input = PayerInfoInput.builder()
            .givenName("John")
            .surname("Doe")
            .email("john.doe@example.com")
            .phoneCountryCode("1")
            .phoneNumber("4155551234")
            .billingAddress(billingAddress)
            .build();

        Map<String, Object> map = input.toGraphQLVariables();

        assertEquals("John", map.get("givenName"));
        assertEquals("Doe", map.get("surname"));
        assertEquals("john.doe@example.com", map.get("email"));
        assertEquals("1", map.get("phoneCountryCode"));
        assertEquals("4155551234", map.get("phoneNumber"));
        assertEquals(billingAddress.toGraphQLVariables(), map.get("billingAddress"));
    }

    @Test
    void testToGraphQLVariablesWithShippingAddress() {
        ShippingAddressInput shippingAddress = ShippingAddressInput.builder()
            .countryCodeAlpha2("US")
            .streetAddress("456 Oak Ave")
            .locality("Los Angeles")
            .region("CA")
            .postalCode("90001")
            .build();

        PayerInfoInput input = PayerInfoInput.builder()
            .givenName("Jane")
            .surname("Smith")
            .email("jane.smith@example.com")
            .shippingAddress(shippingAddress)
            .build();

        Map<String, Object> map = input.toGraphQLVariables();

        assertEquals("Jane", map.get("givenName"));
        assertEquals("Smith", map.get("surname"));
        assertEquals("jane.smith@example.com", map.get("email"));
        assertEquals(shippingAddress.toGraphQLVariables(), map.get("shippingAddress"));
    }
}
