package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CustomerOptionsPayPalRequest;
import com.braintreegateway.CustomerOptionsPayPalShippingRequest;
import com.braintreegateway.CustomerOptionsRequest;

public class CustomerOptionsPayPalShippingRequestTest {

    @Test
    public void buildsShippingXmlForAllFieldsAndChainsBackToParent() {
        CustomerOptionsPayPalRequest parent = new CustomerOptionsPayPalRequest(new CustomerOptionsRequest());
        CustomerOptionsPayPalShippingRequest request = new CustomerOptionsPayPalShippingRequest(parent);

        CustomerOptionsPayPalRequest returned = request
                .company("Braintree")
                .countryName("United States of America")
                .countryCodeAlpha2("US")
                .countryCodeAlpha3("USA")
                .countryCodeNumeric("840")
                .extendedAddress("Suite 200")
                .firstName("Dan")
                .lastName("Schulman")
                .locality("Chicago")
                .postalCode("60622")
                .region("IL")
                .streetAddress("123 Main St")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("paypal shipping xml fields",
                () -> assertTrue(xml.contains("<shipping>"), xml),
                () -> assertTrue(xml.contains("<company>Braintree</company>"), xml),
                () -> assertTrue(xml.contains("<countryName>United States of America</countryName>"), xml),
                () -> assertTrue(xml.contains("<countryCodeAlpha2>US</countryCodeAlpha2>"), xml),
                () -> assertTrue(xml.contains("<countryCodeAlpha3>USA</countryCodeAlpha3>"), xml),
                () -> assertTrue(xml.contains("<countryCodeNumeric>840</countryCodeNumeric>"), xml),
                () -> assertTrue(xml.contains("<extendedAddress>Suite 200</extendedAddress>"), xml),
                () -> assertTrue(xml.contains("<firstName>Dan</firstName>"), xml),
                () -> assertTrue(xml.contains("<lastName>Schulman</lastName>"), xml),
                () -> assertTrue(xml.contains("<locality>Chicago</locality>"), xml),
                () -> assertTrue(xml.contains("<postalCode>60622</postalCode>"), xml),
                () -> assertTrue(xml.contains("<region>IL</region>"), xml),
                () -> assertTrue(xml.contains("<streetAddress>123 Main St</streetAddress>"), xml));
    }
}
