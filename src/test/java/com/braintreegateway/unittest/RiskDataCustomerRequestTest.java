package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.RiskDataCustomerRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;

public class RiskDataCustomerRequestTest {
  @Test
  public void toXMLIncludesAllElements() throws IOException, SAXException {
    RiskDataCustomerRequest request = new RiskDataCustomerRequest()
      .customerIP("1234")
      .customerBrowser("Safari");

    String expectedXML = "<riskData>\n"
                         + "  <customerIP>1234</customerIP>\n"
                         + "  <customerBrowser>Safari</customerBrowser>\n"
                         + "</riskData>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}
