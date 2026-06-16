package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PayPalAccountOptionsRequest;
import com.braintreegateway.PayPalAccountRequest;

public class PayPalAccountOptionsRequestTest {

    @Test
    public void buildsXmlExposesGetterAndChainsBackToParent() {
        PayPalAccountRequest parent = new PayPalAccountRequest();
        PayPalAccountOptionsRequest request = new PayPalAccountOptionsRequest(parent);

        PayPalAccountRequest returned = request.makeDefault(true).done();

        assertSame(parent, returned);
        assertEquals(true, request.getMakeDefault());

        String xml = request.toXML();
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<makeDefault>true</makeDefault>"), xml);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new PayPalAccountOptionsRequest().makeDefault(false).toXML();

        assertTrue(xml.contains("<makeDefault>false</makeDefault>"), xml);
    }
}
