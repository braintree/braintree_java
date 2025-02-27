package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.ShippingOptionRequest;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ShippingOptionRequestTest {
    @Test
  public void testShippingOptionsFields() throws IOException, SAXException {
    ShippingOptionRequest request = new ShippingOptionRequest().
        amount(new BigDecimal("10.00")).
        id("abc123").
        label("fastShip").
        selected(true).
        type("quick");

    String expectedXML = 
        "<shippingOption>\n"
        + " <amount>10.00</amount>\n"
        + " <id>abc123</id>\n"
        + " <label>fastShip</label>\n"
        + " <selected>true</selected>\n"
        + " <type>quick</type>\n"
        + "</shippingOption>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}