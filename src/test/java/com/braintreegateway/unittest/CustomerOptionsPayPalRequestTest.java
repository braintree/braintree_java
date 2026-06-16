package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.CustomerOptionsPayPalRequest;
import com.braintreegateway.CustomerOptionsRequest;

public class CustomerOptionsPayPalRequestTest {

    @Test
    public void buildsXmlForAllFieldsIncludingShippingAndChainsBackToParent() {
        CustomerOptionsRequest parent = new CustomerOptionsRequest();
        CustomerOptionsPayPalRequest request = new CustomerOptionsPayPalRequest(parent);

        CustomerOptionsRequest returned = request
                .payeeEmail("payee@example.com")
                .description("a-description")
                .customField("a-custom-field")
                .orderId("an-order-id")
                .amount(new BigDecimal("10.00"))
                .done();

        assertSame(parent, returned);

        // Exercise nested shipping() builder.
        request.shipping().postalCode("60622").done();

        String xml = request.toXML();
        assertTrue(xml.contains("<paypal>"), xml);
        assertTrue(xml.contains("<payeeEmail>payee@example.com</payeeEmail>"), xml);
        assertTrue(xml.contains("<description>a-description</description>"), xml);
        assertTrue(xml.contains("<customField>a-custom-field</customField>"), xml);
        assertTrue(xml.contains("<orderId>an-order-id</orderId>"), xml);
        assertTrue(xml.contains("<amount>10.00</amount>"), xml);
        assertTrue(xml.contains("<shipping>"), xml);
        assertTrue(xml.contains("<postalCode>60622</postalCode>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("paypal%5Bpayee_email%5D=payee%40example.com"), queryString);
    }
}
