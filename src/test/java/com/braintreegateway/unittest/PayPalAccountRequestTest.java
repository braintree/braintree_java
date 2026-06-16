package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PayPalAccountRequest;

public class PayPalAccountRequestTest {

    @Test
    public void buildsXmlWithTokenAndNestedOptions() {
        PayPalAccountRequest request = new PayPalAccountRequest().token("a-token");
        request.options().makeDefault(true).done();

        String xml = request.toXML();
        assertTrue(xml.contains("<paypalAccount>"), xml);
        assertTrue(xml.contains("<token>a-token</token>"), xml);
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<makeDefault>true</makeDefault>"), xml);
    }

    @Test
    public void omitsOptionsWhenNotSet() {
        String xml = new PayPalAccountRequest().token("a-token").toXML();

        assertTrue(xml.contains("<token>a-token</token>"), xml);
        assertFalse(xml.contains("<options>"), xml);
    }
}
