package com.braintreegateway.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PackageTrackingRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionLineItemRequest;
import com.braintreegateway.TransactionRequest;

// NEXT_MAJOR_VERSION remove paypalTrackingId assertions.
public class PackageTrackingIT extends IntegrationTest {
    @Test
    public void testPackageTracking() {

        // Create Transaction
        TransactionRequest request = new TransactionRequest().amount(new BigDecimal("100.00")).options()
                .submitForSettlement(true).done().paypalAccount().payerId("fake-payer-id").paymentId("fake-payment-id")
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionLineItemRequest packageLine1 = new TransactionLineItemRequest()
                .productCode("ABC 01")
                .quantity(new BigDecimal(1))
                .name("Best Product Ever")
                .description("Best Description Ever")
                .upcCode("93759A49")
                .upcType("UPC-A")
                .imageUrl("https://example.com/image.png");

        TransactionLineItemRequest packageLine2 = new TransactionLineItemRequest()
                .productCode("ABC 02")
                .quantity(new BigDecimal(1))
                .name("Best Product Ever")
                .description("Best Description Ever")
                .upcCode("93486946")
                .upcType("UPC-B")
                .imageUrl("https://example.com/image2.png");

        // Create First package with 2 products
        PackageTrackingRequest firstPackage = new PackageTrackingRequest()
                .carrier("UPS")
                .notifyPayer(true)
                .trackingNumber("tracking_number_1").addLineItem(packageLine1).addLineItem(packageLine2);

        // First package is shipped by the merchant
        Result<Transaction> firstPackageResult = gateway.transaction().packageTracking(transaction.getId(),
                firstPackage);
        assertTrue(firstPackageResult.isSuccess());
        Transaction txnWithFirstPackageTracking = firstPackageResult.getTarget();
        assertNotNull(txnWithFirstPackageTracking.getPackages().get(0).getId());
        assertEquals("UPS", txnWithFirstPackageTracking.getPackages().get(0).getCarrier());
        assertEquals("tracking_number_1", txnWithFirstPackageTracking.getPackages().get(0).getTrackingNumber());
        assertNull(txnWithFirstPackageTracking.getPackages().get(0).getPayPalTrackerId());
        assertNull(txnWithFirstPackageTracking.getPackages().get(0).getPayPalTrackingId());

        // Create second package with 1 product
        PackageTrackingRequest secondPackage = new PackageTrackingRequest()
                .carrier("FEDEX")
                .notifyPayer(true)
                .trackingNumber("tracking_number_2");

        TransactionLineItemRequest packageLine3 = new TransactionLineItemRequest()
                .productCode("ABC 03")
                .quantity(new BigDecimal(1))
                .name("Best Product Ever")
                .description("Best Description Ever");

        secondPackage.addLineItem(packageLine3);

        // Second package is shipped by the merchant
        Result<Transaction> secondPackageResult = gateway.transaction().packageTracking(transaction.getId(),
                secondPackage);
        assertTrue(secondPackageResult.isSuccess());
        Transaction txnWithSecondPackageTracking = secondPackageResult.getTarget();
        assertNotNull(txnWithSecondPackageTracking.getPackages().get(1).getId());
        assertEquals("FEDEX", txnWithSecondPackageTracking.getPackages().get(1).getCarrier());
        assertEquals("tracking_number_2",
                txnWithSecondPackageTracking.getPackages().get(1).getTrackingNumber());
        assertNull(txnWithSecondPackageTracking.getPackages().get(1).getPayPalTrackerId());
        assertNull(txnWithSecondPackageTracking.getPackages().get(1).getPayPalTrackingId());

        // Find transaction gives both packages
        Transaction findTransaction = gateway.transaction().find(transaction.getId());
        assertEquals(2, findTransaction.getPackages().size());
        assertNotNull(findTransaction.getPackages().get(0).getId());
        assertEquals("UPS", findTransaction.getPackages().get(0).getCarrier());
        assertEquals("tracking_number_1", findTransaction.getPackages().get(0).getTrackingNumber());
        assertNull(findTransaction.getPackages().get(0).getPayPalTrackerId());
        assertNull(findTransaction.getPackages().get(0).getPayPalTrackingId());
        assertNotNull(findTransaction.getPackages().get(1).getId());
        assertEquals("FEDEX", findTransaction.getPackages().get(1).getCarrier());
        assertEquals("tracking_number_2", findTransaction.getPackages().get(1).getTrackingNumber());
        assertNull(findTransaction.getPackages().get(1).getPayPalTrackerId());
        assertNull(findTransaction.getPackages().get(1).getPayPalTrackingId());
    }

    @Test
    public void testpackagetrackingRetrievingTrackers() {
        // Retrieve existing transaction trackers
        Transaction findTransaction = gateway.transaction().find("package_tracking_tx");
        assertEquals(2, findTransaction.getPackages().size());
        assertNotNull(findTransaction.getPackages().get(0).getId());
        assertNull(findTransaction.getPackages().get(0).getPayPalTrackingId());
        assertEquals("paypal_tracker_id_1", findTransaction.getPackages().get(0).getPayPalTrackerId());
        assertNotNull(findTransaction.getPackages().get(1).getId());
        assertNull(findTransaction.getPackages().get(1).getPayPalTrackingId());
        assertEquals("paypal_tracker_id_2", findTransaction.getPackages().get(1).getPayPalTrackerId());
    }

    @Test
    public void testPackageTrackingWithInvalidRequest() {
        // Create Transaction
        TransactionRequest request = new TransactionRequest().amount(new BigDecimal("100.00")).options()
                .submitForSettlement(true).done().paypalAccount().payerId("fake-payer-id").paymentId("fake-payment-id")
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        PackageTrackingRequest invalidRequest = new PackageTrackingRequest()
                .trackingNumber("tracking_number_1");

        Result<Transaction> invalidResult = gateway.transaction().packageTracking(transaction.getId(),
                invalidRequest);

        assertFalse(invalidResult.isSuccess());
        assertEquals("Carrier name is required.", invalidResult.getMessage());

        invalidRequest = new PackageTrackingRequest()
                .carrier("UPS");

        invalidResult = gateway.transaction().packageTracking(transaction.getId(),
                invalidRequest);

        assertFalse(invalidResult.isSuccess());
        assertEquals("Tracking number is required.", invalidResult.getMessage());

    }

    @Test
    public void testTransactionParsesWithoutPackages() {
        // Create Transaction
        TransactionRequest request = new TransactionRequest().amount(new BigDecimal("100.00")).options()
                .submitForSettlement(true).done().paypalAccount().payerId("fake-payer-id").paymentId("fake-payment-id")
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        // Find transaction gives no packages
        Transaction findTransaction = gateway.transaction().find(transaction.getId());
        assertEquals(0, findTransaction.getPackages().size());
    }
}
