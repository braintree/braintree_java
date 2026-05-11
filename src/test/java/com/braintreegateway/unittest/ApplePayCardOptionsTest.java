package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.ApplePayCardOptionsRequest;

public class ApplePayCardOptionsTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        ApplePayCardOptionsRequest request = new ApplePayCardOptionsRequest().
            makeDefault(true).
            verifyCard(true).
            verificationAccountType("verification-account-type").
            verificationAmount("verification-amount").
            verificationMerchantAccountId("verification-merchant-account-id");

        String expectedXML = 
            "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "    <verifyCard>true</verifyCard>\n"
            + "    <verificationAccountType>verification-account-type</verificationAccountType>\n"
            + "    <verificationAmount>verification-amount</verificationAmount>\n"
            + "    <verificationMerchantAccountId>verification-merchant-account-id</verificationMerchantAccountId>\n"
            + "  </options>\n";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
