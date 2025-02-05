package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.AmountBreakdownRequest;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PayPalPaymentResourceRequestTest {

  @Test
  public void testPayPalPaymentResourceFields() throws IOException, SAXException {
    TransactionLineItemRequest lineItem = new TransactionLineItemRequest().
            description("Shoes").
            imageUrl("https://example.com/products/23434/pic.png").
            name("Name #1").
            productCode("23434").
            quantity(new BigDecimal("1.0232")).
            totalAmount(new BigDecimal("45.00")).
            unitAmount(new BigDecimal("45.00")).
            upcCode("3878935708DA").
            upcType("UPC-A").
            url("https://example.com/products/23434");

    PayPalPaymentResourceRequest request = new PayPalPaymentResourceRequest().
        amount(new BigDecimal("100.00")).
        amountBreakdown().
            discount(new BigDecimal("15.00")).
            handling(new BigDecimal("0.00")).
            insurance(new BigDecimal("5.00")).
            itemTotal(new BigDecimal("100.00")).
            shipping(new BigDecimal("10.00")).
            shippingDiscount(new BigDecimal("0.00")).
            taxTotal(new BigDecimal("10.00")).
            done().
        currencyIsoCode("USD").
        customField("0437").
        description("This is a test").
        addLineItem(lineItem).
        orderId("order-123456789").
        payeeEmail("usd_merchant@example.com").
        paymentMethodNonce("someNonce").
        shipping().
            firstName("Andrew").
            lastName("Mason").
            company("Braintree Shipping").
            streetAddress("456 W Main St").
            extendedAddress("Apt 2F").
            locality("Bartlett").
            region("MA").
            postalCode("60103").
            countryName("Mexico").
            countryCodeAlpha2("MX").
            countryCodeAlpha3("MEX").
            countryCodeNumeric("484").
            done().
        shippingOption().
            amount(new BigDecimal("10.00")).
            id("option1").
            label("fast").
            selected(true).
            type("SHIPPING").
            done();

    String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<paypalPaymentResource>\n"
        + " <amount>100.00</amount>\n"
        + " <amountBreakdown>\n"
        + "     <discount>15.00</discount>\n"
        + "     <handling>0.00</handling>\n"
        + "     <insurance>5.00</insurance>\n"
        + "     <itemTotal>100.00</itemTotal>\n"
        + "     <shipping>10.00</shipping>\n"
        + "     <shippingDiscount>0.00</shippingDiscount>\n"
        + "     <taxTotal>10.00</taxTotal>\n"
        + " </amountBreakdown>"
        + " <currencyIsoCode>USD</currencyIsoCode>\n"
        + " <customField>0437</customField>\n"
        + " <description>This is a test</description>\n"
        + " <lineItems type=\"array\">"
        + "     <item>\n"
        + "         <description>Shoes</description>\n"
        + "         <imageUrl>https://example.com/products/23434/pic.png</imageUrl>\n"
        + "         <name>Name #1</name>\n"
        + "         <productCode>23434</productCode>\n"
        + "         <quantity>1.0232</quantity>\n"
        + "         <totalAmount>45.00</totalAmount>\n"
        + "         <unitAmount>45.00</unitAmount>\n"
        + "         <upcCode>3878935708DA</upcCode>\n"
        + "         <upcType>UPC-A</upcType>\n"
        + "         <url>https://example.com/products/23434</url>\n"
        + "     </item>\n"
        + " </lineItems>\n"
        + " <orderId>order-123456789</orderId>\n"
        + " <payeeEmail>usd_merchant@example.com</payeeEmail>\n"
        + " <paymentMethodNonce>someNonce</paymentMethodNonce>\n"
        // ......
        + " <shippingOption>\n"
        + "     <amount>10.00</amount>\n"
        + "     <id>abc123</id>\n"
        + "     <label>fastShip</label>\n"
        + "     <selected>true</selected>\n"
        + "     <type>quick</type>\n"
        + " </shippingOption>"
        + "</paypalPaymentResource>\n";

    XMLUnit.setIgnoreWhitespace(true);

    XMLAssert.assertXMLEqual(expectedXML, request.toXML());
  }
}