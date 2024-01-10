package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Transaction;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PackageDetailsTest {
    @Test
    public void parsesTransactionWithPackages() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transaction>\n" +
                "  <id>recognized_transaction_id</id>\n" +
                "  <shipments><shipment>\n" +
                "  <id>track_id</id><tracking-number>tracking_number_1</tracking-number>\n" +
                "  <carrier>UPS</carrier><paypal-tracking-id>pp_tracking_number_1</paypal-tracking-id>" +
                "  </shipment></shipments>\n" +
                "</transaction>\n";

        SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
        Transaction transaction = new Transaction(transactionNode);
        assertEquals("track_id", transaction.getPackages().get(0).getId());
        assertEquals("tracking_number_1", transaction.getPackages().get(0).getTrackingNumber());
        assertEquals("UPS", transaction.getPackages().get(0).getCarrier());
        assertEquals("pp_tracking_number_1", transaction.getPackages().get(0).getPayPalTrackingId());
    }

    @Test
    public void parsesTransactionWithoutPackages() {

        // testing if shipments tag is not present
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transaction>\n" +
                "  <id>recognized_transaction_id</id>\n" +
                "</transaction>\n";

        SimpleNodeWrapper transactionNode = SimpleNodeWrapper.parse(xml);
        Transaction transaction = new Transaction(transactionNode);
        assertEquals(0, transaction.getPackages().size());

        // testing if shipments tag is present but has no shipments
        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transaction>\n" +
                "  <id>recognized_transaction_id</id>\n" +
                "  <shipments></shipments>\n" +
                "</transaction>\n";

        transactionNode = SimpleNodeWrapper.parse(xml);
        transaction = new Transaction(transactionNode);
        assertEquals(0, transaction.getPackages().size());
    }
}