package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.AmountBreakdownRequest;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AmountBreakdownRequestTest {

  @Test
  public void testAmountBreakdownFields() throws IOException, SAXException {
    AmountBreakdownRequest request = new AmountBreakdownRequest().
        discount(new BigDecimal("15.00")).
        handling(new BigDecimal("0.00")).
        insurance(new BigDecimal("5.00")).
        itemTotal(new BigDecimal("100.00")).
        shipping(new BigDecimal("10.00")).
        shippingDiscount(new BigDecimal("0.00")).
        taxTotal(new BigDecimal("10.00"));

    String expectedXML = 
        "<amount_breakdown>"
        + " <discount>15.00</discount>"
        + " <handling>0.00</handling>"
        + " <insurance>5.00</insurance>"
        + " <item_total>100.00</item_total>"
        + " <shipping>10.00</shipping>"
        + " <shipping_discount>0.00</shipping_discount>"
        + " <tax_total>10.00</tax_total>"
        + "</amount_breakdown>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}