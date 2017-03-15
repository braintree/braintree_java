package com.braintreegateway.integrationtest;

import com.braintreegateway.test.Nonce;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IdealPaymentIT extends IntegrationTest {

    @Test
    public void findsIdealPaymentById() {
        String idealPaymentId = TestHelper.generateValidIdealPaymentId(gateway);

        IdealPayment idealPayment = gateway.idealPayment().find(idealPaymentId);

        assertTrue(Pattern.matches("^idealpayment_\\w{6,}$", idealPayment.getId()));
        assertTrue(Pattern.matches("^\\d{16,}$", idealPayment.getIdealTransactionId()));
        assertNotNull(idealPayment.getCurrency());
        assertNotNull(idealPayment.getAmount());
        assertNotNull(idealPayment.getStatus());
        assertNotNull(idealPayment.getOrderId());
        assertNotNull(idealPayment.getIssuer());
        assertTrue(idealPayment.getApprovalUrl().startsWith("https://"));
        assertNotNull(idealPayment.getIbanBankAccount().getAccountHolderName());
        assertNotNull(idealPayment.getIbanBankAccount().getBic());
        assertNotNull(idealPayment.getIbanBankAccount().getMaskedIban());
        assertTrue(Pattern.matches("^\\d{4}$", idealPayment.getIbanBankAccount().getIbanAccountNumberLast4()));
        assertNotNull(idealPayment.getIbanBankAccount().getIbanCountry());
        assertNotNull(idealPayment.getIbanBankAccount().getDescription());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenIdealPaymentIsMissing() {
        try {
            gateway.idealPayment().find(TestHelper.generateInvalidIdealPaymentId());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void saleWithIdealPayment() {
        String idealPaymentId = TestHelper.generateValidIdealPaymentId(gateway);

        TransactionRequest transactionRequest = new TransactionRequest()
            .merchantAccountId("ideal_merchant_account")
            .orderId("ABC123")
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount);

        Result<Transaction> transactionResult = gateway.idealPayment().sale(idealPaymentId, transactionRequest);
        assertTrue(transactionResult.isSuccess());
        Transaction transaction = transactionResult.getTarget();
        assertNotNull(transaction);

        assertEquals(Transaction.Status.SETTLED, transaction.getStatus());

        IdealPaymentDetails idealPaymentDetails = transaction.getIdealPaymentDetails();
        assertTrue(Pattern.matches("^idealpayment_\\w{6,}$", idealPaymentDetails.getIdealPaymentId()));
        assertTrue(Pattern.matches("^\\d{16,}$", idealPaymentDetails.getIdealTransactionId()));
        assertTrue(idealPaymentDetails.getImageUrl().startsWith("https://"));
        assertNotNull(idealPaymentDetails.getMaskedIban());
        assertNotNull(idealPaymentDetails.getBic());
    }
}
