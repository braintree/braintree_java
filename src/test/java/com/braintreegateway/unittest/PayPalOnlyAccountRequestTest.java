package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.MerchantRequest;
import com.braintreegateway.PayPalOnlyAccountRequest;

public class PayPalOnlyAccountRequestTest {

    @Test
    public void buildsXmlExposesGettersAndChainsBackToParent() {
        MerchantRequest parent = new MerchantRequest();
        PayPalOnlyAccountRequest request = new PayPalOnlyAccountRequest(parent);

        MerchantRequest returned = request
                .clientId("a-client-id")
                .clientSecret("a-client-secret")
                .done();

        assertSame(parent, returned);

        assertAll("getters",
                () -> assertEquals("a-client-id", request.getClientId()),
                () -> assertEquals("a-client-secret", request.getClientSecret()));

        String xml = request.toXML();
        assertTrue(xml.contains("<paypalAccount>"), xml);
        assertTrue(xml.contains("<clientId>a-client-id</clientId>"), xml);
        assertTrue(xml.contains("<clientSecret>a-client-secret</clientSecret>"), xml);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        assertTrue(new PayPalOnlyAccountRequest().clientId("a-client-id").toXML()
                .contains("<clientId>a-client-id</clientId>"));
    }
}
