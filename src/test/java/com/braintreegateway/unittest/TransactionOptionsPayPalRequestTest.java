package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionOptionsPayPalRequest;
import com.braintreegateway.TransactionOptionsRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionOptionsPayPalRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        TransactionOptionsRequest parent = new TransactionOptionsRequest(new TransactionRequest());
        TransactionOptionsPayPalRequest request = new TransactionOptionsPayPalRequest(parent);

        TransactionOptionsRequest returned = request
                .customField("a-custom-field")
                .description("a-description")
                .payeeId("a-payee-id")
                .payeeEmail("payee@example.com")
                .supplementaryData("key1", "value1")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertAll("paypal options xml",
                () -> assertTrue(xml.contains("<paypal>"), xml),
                () -> assertTrue(xml.contains("<customField>a-custom-field</customField>"), xml),
                () -> assertTrue(xml.contains("<description>a-description</description>"), xml),
                () -> assertTrue(xml.contains("<payeeId>a-payee-id</payeeId>"), xml),
                () -> assertTrue(xml.contains("<payeeEmail>payee@example.com</payeeEmail>"), xml),
                () -> assertTrue(xml.contains("<supplementaryData>"), xml),
                () -> assertTrue(xml.contains("<key1>value1</key1>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("a-custom-field"), queryString);
    }

    @Test
    public void omitsSupplementaryDataWhenEmpty() {
        TransactionOptionsPayPalRequest request =
                new TransactionOptionsPayPalRequest(new TransactionOptionsRequest(new TransactionRequest()))
                        .customField("a-custom-field");

        String xml = request.toXML();
        assertTrue(xml.contains("<customField>a-custom-field</customField>"), xml);
        assertFalse(xml.contains("supplementaryData"), xml);
    }
}
