package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.CreditCardVerificationRequest;
import com.braintreegateway.CreditCardVerificationOptionsRequest;

public class CreditCardVerificationOptionsRequestTest {
    @Test
    public void toXmlIncludesAccountInformationInquiry() throws IOException, SAXException {
        CreditCardVerificationRequest creditcardverification = new CreditCardVerificationRequest();
        CreditCardVerificationOptionsRequest request = new CreditCardVerificationOptionsRequest(creditcardverification)
            .accountInformationInquiry("send_data");

        String expectedXML =
            "  <options>\n"
            + "    <accountInformationInquiry>send_data</accountInformationInquiry>\n"
            + "  </options>\n";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
