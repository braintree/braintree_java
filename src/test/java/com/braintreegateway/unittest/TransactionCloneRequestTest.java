package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.TransactionCloneRequest;

public class TransactionCloneRequestTest {

    @Test
    public void buildsXmlWithAllFieldsAndNestedOptions() {
        TransactionCloneRequest request = new TransactionCloneRequest()
                .amount(new BigDecimal("10.00"))
                .channel("a-channel");

        request.options().submitForSettlement(true).done();

        String xml = request.toXML();
        assertTrue(xml.contains("<transactionClone>"), xml);
        assertTrue(xml.contains("<amount>10.00</amount>"), xml);
        assertTrue(xml.contains("<channel>a-channel</channel>"), xml);
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<submitForSettlement>true</submitForSettlement>"), xml);
    }

    @Test
    public void omitsOptionsWhenNotSet() {
        String xml = new TransactionCloneRequest().amount(new BigDecimal("10.00")).toXML();

        assertTrue(xml.contains("<amount>10.00</amount>"), xml);
        assertFalse(xml.contains("<options>"), xml);
    }
}
