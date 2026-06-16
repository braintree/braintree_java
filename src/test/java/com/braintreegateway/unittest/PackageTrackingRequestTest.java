package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PackageTrackingRequest;
import com.braintreegateway.TransactionLineItemRequest;

public class PackageTrackingRequestTest {

    @Test
    public void buildsXmlWithAllFieldsAndLineItems() {
        TransactionLineItemRequest lineItem = new TransactionLineItemRequest()
                .name("a-product")
                .quantity(new BigDecimal("1"));

        PackageTrackingRequest request = new PackageTrackingRequest()
                .carrier("UPS")
                .trackingNumber("1Z999AA10123456784")
                .notifyPayer(true)
                .addLineItem(lineItem);

        String xml = request.toXML();
        assertTrue(xml.contains("<shipment>"), xml);
        assertTrue(xml.contains("<carrier>UPS</carrier>"), xml);
        assertTrue(xml.contains("<trackingNumber>1Z999AA10123456784</trackingNumber>"), xml);
        assertTrue(xml.contains("<notifyPayer>true</notifyPayer>"), xml);
        assertTrue(xml.contains("<lineItems type=\"array\">"), xml);
        assertTrue(xml.contains("<name>a-product</name>"), xml);

        String queryString = request.toQueryString();
        assertTrue(queryString.contains("shipment%5Bcarrier%5D=UPS"), queryString);
        assertTrue(queryString.contains("shipment%5Btracking_number%5D=1Z999AA10123456784"), queryString);
        assertTrue(queryString.contains("shipment%5Bnotify_payer%5D=true"), queryString);
    }

    @Test
    public void omitsLineItemsWhenEmpty() {
        String xml = new PackageTrackingRequest().carrier("UPS").toXML();

        assertTrue(xml.contains("<carrier>UPS</carrier>"), xml);
        assertFalse(xml.contains("lineItems"), xml);
    }
}
