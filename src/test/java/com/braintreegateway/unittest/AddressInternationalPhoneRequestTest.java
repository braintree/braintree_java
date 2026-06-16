package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AddressInternationalPhoneRequest;
import com.braintreegateway.AddressRequest;

public class AddressInternationalPhoneRequestTest {

    @Test
    public void buildsXmlAndChainsBackToParent() {
        AddressRequest parent = new AddressRequest();
        AddressInternationalPhoneRequest request = new AddressInternationalPhoneRequest(parent);

        AddressRequest returned = request
                .countryCode("1")
                .nationalNumber("3125551234")
                .done();

        assertSame(parent, returned);

        String xml = request.toXML();
        assertTrue(xml.contains("<internationalPhone>"), xml);
        assertTrue(xml.contains("<countryCode>1</countryCode>"), xml);
        assertTrue(xml.contains("<nationalNumber>3125551234</nationalNumber>"), xml);
    }

    @Test
    public void noArgConstructorBuildsXml() {
        String xml = new AddressInternationalPhoneRequest()
                .countryCode("44")
                .nationalNumber("7911123456")
                .toXML();

        assertTrue(xml.contains("<countryCode>44</countryCode>"), xml);
        assertTrue(xml.contains("<nationalNumber>7911123456</nationalNumber>"), xml);
    }
}
