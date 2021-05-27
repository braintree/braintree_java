package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.RiskDataCustomerRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import org.junit.jupiter.api.Test;

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
