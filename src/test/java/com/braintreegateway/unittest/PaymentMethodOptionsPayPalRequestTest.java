package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PaymentMethodOptionsPayPalRequest;
import com.braintreegateway.PaymentMethodOptionsRequest;

public class PaymentMethodOptionsPayPalRequestTest {

    @Test
    public void buildsXmlForAllFieldsAndChainsBackToParent() {
        PaymentMethodOptionsRequest parent = new PaymentMethodOptionsRequest();
        PaymentMethodOptionsPayPalRequest request = new PaymentMethodOptionsPayPalRequest(parent);

        PaymentMethodOptionsRequest returned = request
                .payeeEmail("payee@example.com")
                .description("a-description")
                .customField("a-custom-field")
                .orderId("an-order-id")
                .amount(new BigDecimal("10.00"))
                .done();

        assertSame(parent, returned);
        // Exercise nested shipping() builder so it is represented in the paypal XML.
        request.shipping().postalCode("60622").done();

        String xml = request.toXML();
        assertAll("paypal options xml",
                () -> assertTrue(xml.contains("<paypal>"), xml),
                () -> assertTrue(xml.contains("<payeeEmail>payee@example.com</payeeEmail>"), xml),
                () -> assertTrue(xml.contains("<description>a-description</description>"), xml),
                () -> assertTrue(xml.contains("<customField>a-custom-field</customField>"), xml),
                () -> assertTrue(xml.contains("<orderId>an-order-id</orderId>"), xml),
                () -> assertTrue(xml.contains("<amount>10.00</amount>"), xml),
                () -> assertTrue(xml.contains("<shipping>"), xml),
                () -> assertTrue(xml.contains("<postalCode>60622</postalCode>"), xml));

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("a-custom-field"), queryString);
    }
}
