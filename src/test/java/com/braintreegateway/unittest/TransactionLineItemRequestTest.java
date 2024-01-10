package com.braintreegateway.unittest;

import com.braintreegateway.TransactionLineItemRequest;
import com.braintreegateway.TransactionLineItem;

import java.math.BigDecimal;
import java.io.IOException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class TransactionLineItemRequestTest {
    @Test
    public void testSerializesToXML() throws SAXException, IOException {

        TransactionLineItemRequest request = new TransactionLineItemRequest()
            .commodityCode("9SAASSD8724")
            .discountAmount(new BigDecimal("1.02"))
            .imageUrl("https://google.com/image.png")
            .kind(TransactionLineItem.Kind.DEBIT)
            .name("Name #1")
            .productCode("23434")
            .quantity(new BigDecimal("1.2322"))
            .taxAmount(new BigDecimal("4.55"))
            .totalAmount(new BigDecimal("45.15"))
            .unitAmount(new BigDecimal("1.23"))
            .unitOfMeasure("gallon")
            .upcCode("3878935708DA")
            .upcType("UPC-A")
            .url("https://example.com/products/23434");


        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<item>\n"
            + "  <commodityCode>9SAASSD8724</commodityCode>\n"
            + "  <discountAmount>1.02</discountAmount>\n"
            + "  <imageUrl>https://google.com/image.png</imageUrl>\n"
            + "  <kind>debit</kind>\n"
            + "  <name>Name #1</name>\n"
            + "  <productCode>23434</productCode>\n"
            + "  <quantity>1.2322</quantity>\n"
            + "  <taxAmount>4.55</taxAmount>\n"
            + "  <totalAmount>45.15</totalAmount>\n"
            + "  <unitAmount>1.23</unitAmount>\n"
            + "  <unitOfMeasure>gallon</unitOfMeasure>\n"
            + "  <upcCode>3878935708DA</upcCode>\n"
            + "  <upcType>UPC-A</upcType>\n"
            + "  <url>https://example.com/products/23434</url>\n"
            + "</item>\n";

        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(expectedXML, request.toXML());

    }
}
