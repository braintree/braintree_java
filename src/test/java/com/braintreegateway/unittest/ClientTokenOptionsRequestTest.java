package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ClientTokenOptionsRequest;
import com.braintreegateway.ClientTokenRequest;

public class ClientTokenOptionsRequestTest {

    @Test
    public void buildsXmlExposesGettersAndChainsBackToParent() {
        ClientTokenRequest parent = new ClientTokenRequest();
        ClientTokenOptionsRequest request = new ClientTokenOptionsRequest(parent);

        ClientTokenRequest returned = request
                .makeDefault(true)
                .verifyCard(false)
                .failOnDuplicatePaymentMethod(true)
                .failOnDuplicatePaymentMethodForCustomer(false)
                .done();

        assertSame(parent, returned);

        assertAll("getters",
                () -> assertEquals(true, request.getMakeDefault()),
                () -> assertEquals(false, request.getVerifyCard()),
                () -> assertEquals(true, request.getFailOnDuplicatePaymentMethod()),
                () -> assertEquals(false, request.getFailOnDuplicatePaymentMethodForCustomer()));

        String xml = request.toXML();
        assertTrue(xml.contains("<options>"), xml);
        assertTrue(xml.contains("<makeDefault>true</makeDefault>"), xml);
        assertTrue(xml.contains("<verifyCard>false</verifyCard>"), xml);
        assertTrue(xml.contains("<failOnDuplicatePaymentMethod>true</failOnDuplicatePaymentMethod>"), xml);
        assertTrue(xml.contains("<failOnDuplicatePaymentMethodForCustomer>false</failOnDuplicatePaymentMethodForCustomer>"), xml);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new ClientTokenOptionsRequest().makeDefault(true).toXML();

        assertTrue(xml.contains("<makeDefault>true</makeDefault>"), xml);
    }
}
