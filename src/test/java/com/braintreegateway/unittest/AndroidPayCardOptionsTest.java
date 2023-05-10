package com.braintreegateway.unittest;

import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.AndroidPayCardOptionsRequest;

public class AndroidPayCardOptionsTest {
    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        AndroidPayCardOptionsRequest request = new AndroidPayCardOptionsRequest().
            makeDefault(true);

        String expectedXML = 
            "  <options>\n"
            + "    <makeDefault>true</makeDefault>\n"
            + "  </options>\n";

        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(expectedXML, request.toXML());
    }
}
