package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CreditCardVerificationCreditCardRequest;
import com.braintreegateway.CreditCardVerificationRequest;

public class CreditCardVerificationCreditCardRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        CreditCardVerificationRequest parent = new CreditCardVerificationRequest();
        CreditCardVerificationCreditCardRequest request = new CreditCardVerificationCreditCardRequest(parent);

        CreditCardVerificationRequest returned = request
                .cardholderName("Dan Schulman")
                .cvv("123")
                .number("4111111111111111")
                .expirationDate("05/2025")
                .expirationMonth("05")
                .expirationYear("2025")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<cardholderName>Dan Schulman</cardholderName>"), xml);
        assertTrue(xml.contains("<cvv>123</cvv>"), xml);
        assertTrue(xml.contains("<number>4111111111111111</number>"), xml);
        assertTrue(xml.contains("<expirationDate>05/2025</expirationDate>"), xml);
        assertTrue(xml.contains("<expirationMonth>05</expirationMonth>"), xml);
        assertTrue(xml.contains("<expirationYear>2025</expirationYear>"), xml);
    }

    @Test
    public void buildsNestedBillingAddress() {
        CreditCardVerificationCreditCardRequest request =
                new CreditCardVerificationCreditCardRequest(new CreditCardVerificationRequest());

        request.number("4111111111111111")
                .billingAddress()
                    .postalCode("60622")
                    .done();

        String xml = request.toXML();
        assertTrue(xml.contains("<billingAddress>"), xml);
        assertTrue(xml.contains("<postalCode>60622</postalCode>"), xml);
    }
}
