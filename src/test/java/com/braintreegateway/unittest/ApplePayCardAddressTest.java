package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.ApplePayCardAddressRequest;
import com.braintreegateway.ApplePayCardRequest;

public class ApplePayCardAddressTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        ApplePayCardAddressRequest request = new ApplePayCardAddressRequest(new ApplePayCardRequest()).
            company("some-company").
            countryCodeAlpha2("country-code2").
            countryCodeAlpha3("country-code3").
            countryCodeNumeric("country-code").
            countryName("country-name").
            extendedAddress("extended-address").
            firstName("first-name").
            lastName("last-name").
            locality("locality").
            postalCode("11111").
            phoneNumber("phone-number").
            region("region").
            streetAddress("street-address");

        String expectedXML = 
            "<billingAddress>\n"
            + "  <company>some-company</company>\n"
            + "  <countryCodeAlpha2>country-code2</countryCodeAlpha2>\n"
            + "  <countryCodeAlpha3>country-code3</countryCodeAlpha3>\n"
            + "  <countryCodeNumeric>country-code</countryCodeNumeric>\n"
            + "  <countryName>country-name</countryName>\n"
            + "  <extendedAddress>extended-address</extendedAddress>\n"
            + "  <firstName>first-name</firstName>\n"
            + "  <lastName>last-name</lastName>\n"
            + "  <locality>locality</locality>\n"
            + "  <postalCode>11111</postalCode>\n"
            + "  <phoneNumber>phone-number</phoneNumber>\n"
            + "  <region>region</region>\n"
            + "  <streetAddress>street-address</streetAddress>\n"
            + "</billingAddress>";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
