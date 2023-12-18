package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.AndroidPayNetworkTokenOptionsRequest;

public class AndroidPayNetworkTokenOptionsTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        AndroidPayNetworkTokenOptionsRequest request = new AndroidPayNetworkTokenOptionsRequest().
            makeDefault(true);

        String expectedXML = 
            "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "  </options>\n";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
