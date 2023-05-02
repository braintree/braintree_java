package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.ApplePayCardRequest;

public class ApplePayCardRequestTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        ApplePayCardRequest request = new ApplePayCardRequest().
            billingAddress().
                postalCode("11111").
                done().
            cardholderName("cardholder-name").
            cryptogram("cryptogram").
            customerId("customer-id").
            eciIndicator("eci-indicator").
            expirationMonth("expiration-month").
            expirationYear("expiration-year").
            number("number").
            options().
                makeDefault(true).
                done().
            token("token");

        String expectedXML = 
            "<applePayCard>\n"
            + "  <billingAddress>\n"
            + "    <postalCode>11111</postalCode>\n"
            + "  </billingAddress>\n"
            + "  <cardholderName>cardholder-name</cardholderName>\n"
            + "  <cryptogram>cryptogram</cryptogram>\n"
            + "  <customerId>customer-id</customerId>\n"
            + "  <eciIndicator>eci-indicator</eciIndicator>\n"
            + "  <expirationMonth>expiration-month</expirationMonth>\n"
            + "  <expirationYear>expiration-year</expirationYear>\n"
            + "  <number>number</number>\n"
            + "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "  </options>\n"
            + "  <token>token</token>\n"
            + "</applePayCard>";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
