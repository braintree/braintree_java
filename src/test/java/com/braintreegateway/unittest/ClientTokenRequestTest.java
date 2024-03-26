package com.braintreegateway.unittest;

import java.io.IOException;
import java.util.ArrayList;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.ClientTokenOptionsRequest;

public class ClientTokenRequestTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        ArrayList<String> domains = new ArrayList<String>();
        domains.add("example.com");
        ClientTokenRequest request = new ClientTokenRequest().
            customerId("abc123").
            domains(domains).
            merchantAccountId("987654321").
            options(new ClientTokenOptionsRequest().
                makeDefault(true).
                verifyCard(false).
                failOnDuplicatePaymentMethod(true)).
            version(2);

        String expectedXML = 
            "<clientToken>\n"
            + "  <customerId>abc123</customerId>\n"
            + "  <domains type=\"array\"><item>example.com</item></domains>\n"
            + "  <merchantAccountId>987654321</merchantAccountId>\n"
            + "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "    <verifyCard>false</verifyCard>\n"
            + "    <failOnDuplicatePaymentMethod>true</failOnDuplicatePaymentMethod>\n"
            + "  </options>\n"
            + "  <version>2</version>\n"
            + "</clientToken>";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}