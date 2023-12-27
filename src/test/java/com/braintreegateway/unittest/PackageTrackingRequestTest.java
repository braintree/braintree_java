package com.braintreegateway.unittest;

import java.io.IOException;
import java.math.BigDecimal;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.braintreegateway.PackageTrackingRequest;
import com.braintreegateway.TransactionLineItemRequest;

public class PackageTrackingRequestTest {

        @Test
        public void testSerializesToXML() throws SAXException, IOException {

                TransactionLineItemRequest packageLine1 = new TransactionLineItemRequest()
                                .productCode("ABC 01")
                                .quantity(new BigDecimal(1))
                                .name("Best Product Ever")
                                .description("Best Description Ever");

                TransactionLineItemRequest packageLine2 = new TransactionLineItemRequest()
                                .productCode("ABC 02")
                                .quantity(new BigDecimal(1))
                                .name("Best Product Ever")
                                .description("Best Description Ever");

                PackageTrackingRequest request = new PackageTrackingRequest()
                                .carrier("UPS")
                                .notifyPayer(true)
                                .trackingNumber("tracking_number_1").addLineItem(packageLine1)
                                .addLineItem(packageLine2);

                String expectedXML = "<shipment>\n"
                                + "   <trackingNumber>tracking_number_1</trackingNumber>\n"
                                + "   <carrier>UPS</carrier>\n"
                                + "   <notifyPayer>true</notifyPayer>\n"
                                + "   <lineItems type=\"array\">\n"
                                + "           <item>\n"
                                + "                   <quantity>1</quantity>\n"
                                + "                   <name>Best Product Ever</name>\n"
                                + "                   <description>Best Description Ever</description>\n"
                                + "                   <productCode>ABC 01</productCode>\n"
                                + "           </item>\n"
                                + "           <item>\n"
                                + "                   <quantity>1</quantity>\n"
                                + "                   <name>Best Product Ever</name>\n"
                                + "                   <description>Best Description Ever</description>\n"
                                + "                   <productCode>ABC 02</productCode>\n"
                                + "           </item>\n"
                                + "   </lineItems>\n"
                                + "</shipment>";

                XMLUnit.setIgnoreWhitespace(true);

                XMLAssert.assertXMLEqual(expectedXML, request.toXML());
        }
}