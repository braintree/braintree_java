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
        "<amountBreakdown>\n"
        + " <discount>15.00</discount>\n"
        + " <handling>0.00</handling>\n"
        + " <insurance>5.00</insurance>\n"
        + " <itemTotal>100.00</itemTotal>\n"
        + " <shipping>10.00</shipping>\n"
        + " <shippingDiscount>0.00</shippingDiscount>\n"
        + " <taxTotal>10.00</taxTotal>\n"
        + "</amountBreakdown>";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}