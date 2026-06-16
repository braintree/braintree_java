package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardVerificationBillingAddressRequest;
import com.braintreegateway.CreditCardVerificationCreditCardRequest;
import com.braintreegateway.CreditCardVerificationRequest;

public class CreditCardVerificationBillingAddressRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        CreditCardVerificationCreditCardRequest parent =
                new CreditCardVerificationCreditCardRequest(new CreditCardVerificationRequest());
        CreditCardVerificationBillingAddressRequest request =
                new CreditCardVerificationBillingAddressRequest(parent);

        CreditCardVerificationCreditCardRequest returned = request
                .company("Braintree")
                .countryCodeAlpha2("US")
                .countryCodeAlpha3("USA")
                .countryCodeNumeric("840")
                .countryName("United States of America")
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
        assertTrue(xml.contains("<company>Braintree</company>"), xml);
        assertTrue(xml.contains("<countryCodeAlpha2>US</countryCodeAlpha2>"), xml);
        assertTrue(xml.contains("<countryCodeAlpha3>USA</countryCodeAlpha3>"), xml);
        assertTrue(xml.contains("<countryCodeNumeric>840</countryCodeNumeric>"), xml);
        assertTrue(xml.contains("<countryName>United States of America</countryName>"), xml);
        assertTrue(xml.contains("<extendedAddress>Suite 200</extendedAddress>"), xml);
        assertTrue(xml.contains("<firstName>Dan</firstName>"), xml);
        assertTrue(xml.contains("<lastName>Schulman</lastName>"), xml);
        assertTrue(xml.contains("<locality>Chicago</locality>"), xml);
        assertTrue(xml.contains("<postalCode>60622</postalCode>"), xml);
        assertTrue(xml.contains("<region>IL</region>"), xml);
        assertTrue(xml.contains("<streetAddress>123 Main St</streetAddress>"), xml);
    }
}
