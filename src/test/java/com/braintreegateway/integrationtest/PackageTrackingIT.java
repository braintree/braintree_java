package com.braintreegateway.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.PackageTrackingRequest;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionLineItemRequest;
import com.braintreegateway.TransactionRequest;

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
                .description("Best Description Ever");

        TransactionLineItemRequest packageLine2 = new TransactionLineItemRequest()
                .productCode("ABC 02")
                .quantity(new BigDecimal(1))
                .name("Best Product Ever")
                .description("Best Description Ever");

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

        // Find transaction gives both packages
        Transaction findTransaction = gateway.transaction().find(transaction.getId());
        assertEquals(2, findTransaction.getPackages().size());
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