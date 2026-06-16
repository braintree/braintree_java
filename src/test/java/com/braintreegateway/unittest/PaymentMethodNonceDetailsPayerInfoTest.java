package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodNonceDetailsPayerInfo;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PaymentMethodNonceDetailsPayerInfoTest {

    @Test
    public void parsesFromNodeWithAddresses() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<payer-info>" +
                "<email>payer@example.com</email>" +
                "<first-name>Dan</first-name>" +
                "<last-name>Schulman</last-name>" +
                "<payer-id>a-payer-id</payer-id>" +
                "<country-code>US</country-code>" +
                "<billing-address>" +
                "<street-address>123 Main St</street-address>" +
                "<locality>Chicago</locality>" +
                "<region>IL</region>" +
                "<postal-code>60622</postal-code>" +
                "</billing-address>" +
                "<shipping-address>" +
                "<street-address>456 Oak Ave</street-address>" +
                "<locality>Chicago</locality>" +
                "</shipping-address>" +
                "</payer-info>");

        PaymentMethodNonceDetailsPayerInfo info = new PaymentMethodNonceDetailsPayerInfo(node);

        assertAll("payer info from node",
                () -> assertEquals("payer@example.com", info.getEmail()),
                () -> assertEquals("Dan", info.getFirstName()),
                () -> assertEquals("Schulman", info.getLastName()),
                () -> assertEquals("a-payer-id", info.getPayerId()),
                () -> assertEquals("US", info.getCountryCode()),
                () -> assertEquals("123 Main St", info.getBillingAddress().getStreetAddress()),
                () -> assertEquals("456 Oak Ave", info.getShippingAddress().getStreetAddress()));
    }

    @Test
    public void parsesFromNodeWithBlankAddresses() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<payer-info>" +
                "<email>payer@example.com</email>" +
                "<billing-address nil=\"true\"/>" +
                "<shipping-address nil=\"true\"/>" +
                "</payer-info>");

        PaymentMethodNonceDetailsPayerInfo info = new PaymentMethodNonceDetailsPayerInfo(node);

        assertEquals("payer@example.com", info.getEmail());
        assertNull(info.getBillingAddress());
        assertNull(info.getShippingAddress());
    }

    @Test
    public void parsesFromMap() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "payer@example.com");
        map.put("first-name", "Dan");
        map.put("last-name", "Schulman");
        map.put("payer-id", "a-payer-id");
        map.put("country-code", "US");

        PaymentMethodNonceDetailsPayerInfo info = new PaymentMethodNonceDetailsPayerInfo(map);

        assertAll("payer info from map",
                () -> assertEquals("payer@example.com", info.getEmail()),
                () -> assertEquals("Dan", info.getFirstName()),
                () -> assertEquals("Schulman", info.getLastName()),
                () -> assertEquals("a-payer-id", info.getPayerId()),
                () -> assertEquals("US", info.getCountryCode()),
                () -> assertNull(info.getBillingAddress()),
                () -> assertNull(info.getShippingAddress()));
    }
}
