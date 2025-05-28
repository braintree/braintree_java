package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.CreditCardOptionsRequest;

public class CreditCardOptionsRequestTest {
    @Test
    public void toXmlIncludesAccountInformationInquiry() throws IOException, SAXException {
        CreditCardRequest creditCardRequest = new CreditCardRequest();
        CreditCardOptionsRequest request = new CreditCardOptionsRequest(creditCardRequest)
            .accountInformationInquiry("send_data");

        String expectedXML =
            "  <options>\n"
            + "    <accountInformationInquiry>send_data</accountInformationInquiry>\n"
            + "  </options>\n";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
