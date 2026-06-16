package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionPayPalRequest;
import com.braintreegateway.TransactionRequest;

public class TransactionPayPalRequestTest {

    @Test
    public void buildsXmlExposesGettersAndChainsBackToParent() {
        TransactionRequest parent = new TransactionRequest();
        TransactionPayPalRequest request = new TransactionPayPalRequest(parent);

        TransactionRequest returned = request
                .payeeId("a-payee-id")
                .payeeEmail("payee@example.com")
                .payerId("a-payer-id")
                .paymentId("a-payment-id")
                .done();

        assertSame(parent, returned);

        assertAll("getters",
                () -> assertEquals("a-payee-id", request.getPayeeId()),
                () -> assertEquals("payee@example.com", request.getPayeeEmail()),
                () -> assertEquals("a-payer-id", request.getPayerId()),
                () -> assertEquals("a-payment-id", request.getPaymentId()));

        String xml = request.toXML();
        assertAll("paypalAccount xml",
                () -> assertTrue(xml.contains("<paypalAccount>"), xml),
                () -> assertTrue(xml.contains("<payeeId>a-payee-id</payeeId>"), xml),
                () -> assertTrue(xml.contains("<payeeEmail>payee@example.com</payeeEmail>"), xml),
                () -> assertTrue(xml.contains("<payerId>a-payer-id</payerId>"), xml),
                () -> assertTrue(xml.contains("<paymentId>a-payment-id</paymentId>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("a-payee-id"), queryString);
    }
}
