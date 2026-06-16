package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionApplePayCardRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionApplePayCardRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        TransactionApplePayCardRequest request = new TransactionApplePayCardRequest(parent);

        TransactionRequest returned = request
                .number("4111111111111111")
                .cardholderName("Dan Schulman")
                .cryptogram("a-cryptogram")
                .expirationMonth("05")
                .expirationYear("2025")
                .eciIndicator("07")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("apple pay card xml",
                () -> assertTrue(xml.contains("<applePayCard>"), xml),
                () -> assertTrue(xml.contains("<number>4111111111111111</number>"), xml),
                () -> assertTrue(xml.contains("<cardholderName>Dan Schulman</cardholderName>"), xml),
                () -> assertTrue(xml.contains("<cryptogram>a-cryptogram</cryptogram>"), xml),
                () -> assertTrue(xml.contains("<expirationMonth>05</expirationMonth>"), xml),
                () -> assertTrue(xml.contains("<expirationYear>2025</expirationYear>"), xml),
                () -> assertTrue(xml.contains("<eciIndicator>07</eciIndicator>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("4111111111111111"), queryString);
    }
}
