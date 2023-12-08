package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.AndroidPayCardRequest;

public class AndroidPayCardRequestTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        AndroidPayCardRequest request = new AndroidPayCardRequest().
            billingAddress().
                postalCode("11111").
                done().
            cardholderName("cardholder-name").
            cryptogram("cryptogram").
            customerId("customer-id").
            eciIndicator("eci-indicator").
            expirationMonth("expiration-month").
            expirationYear("expiration-year").
            googleTransactionId("transaction-id").
            number("number").
            options().
                makeDefault(true).
                done().
            token("token");

        String expectedXML = 
            "<androidPayCard>\n"
            + "  <billingAddress>\n"
            + "    <postalCode>11111</postalCode>\n"
            + "  </billingAddress>\n"
            + "  <cardholderName>cardholder-name</cardholderName>\n"
            + "  <cryptogram>cryptogram</cryptogram>\n"
            + "  <customerId>customer-id</customerId>\n"
            + "  <eciIndicator>eci-indicator</eciIndicator>\n"
            + "  <expirationMonth>expiration-month</expirationMonth>\n"
            + "  <expirationYear>expiration-year</expirationYear>\n"
            + "  <googleTransactionId>transaction-id</googleTransactionId>\n"
            + "  <number>number</number>\n"
            + "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "  </options>\n"
            + "  <token>token</token>\n"
            + "</androidPayCard>";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
