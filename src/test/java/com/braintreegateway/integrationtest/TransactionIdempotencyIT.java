package com.braintreegateway.integrationtest;

import java.math.BigDecimal;
import java.util.Random;

import com.braintreegateway.Result;
import com.braintreegateway.SandboxValues;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRefundRequest;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.TransactionVoidRequest;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionIdempotencyIT extends IntegrationTest implements MerchantAccountTestConstants {

    private static String LONG_STRING_20 = "12345678901234567890";
    private static String LONG_STRING_100 = LONG_STRING_20 + LONG_STRING_20 + LONG_STRING_20 + LONG_STRING_20 + LONG_STRING_20;
    private static String LONG_STRING_240 = LONG_STRING_100 + LONG_STRING_100 + LONG_STRING_20 + LONG_STRING_20;
    private static String LONG_STRING_255 = LONG_STRING_240 + "12345678" + (new Random().nextInt(9000000) + 1000000);
    private static String LONG_STRING_256 = LONG_STRING_240 + "123456789" + (new Random().nextInt(9000000) + 1000000);


    @Test
    public void saleWithApiRequestKeyReturnsOriginalTransactionOnDuplicateRequest() {
        String apiRequestKey = "idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(apiRequestKey).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result1 = gateway.transaction().sale(request);
        assertTrue(result1.isSuccess());
        Transaction transaction1 = result1.getTarget();
        assertNotNull(transaction1.getId());

        Result<Transaction> result2 = gateway.transaction().sale(request);
        assertTrue(result2.isSuccess());
        Transaction transaction2 = result2.getTarget();

        assertEquals(transaction1.getStatus(), transaction2.getStatus());
        assertEquals(transaction1.getId(), transaction2.getId());
    }

    @Test
    public void saleWithApiRequestKeyFailsWhenDifferentRequestUsedWithSameKey() {
        String apiRequestKey = "idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest request1 = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(apiRequestKey).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result1 = gateway.transaction().sale(request1);
        assertTrue(result1.isSuccess());

        TransactionRequest request2 = new TransactionRequest().
            amount(new BigDecimal("200.00")).
            apiRequestKey(apiRequestKey).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result2 = gateway.transaction().sale(request2);

        assertFalse(result2.isSuccess());
        assertNotNull(result2.getErrors());
        assertNotNull(result2.getErrors().getAllDeepValidationErrors());
        assertNotNull(result2.getErrors().getAllDeepValidationErrors().get(0));
        assertSame(ValidationErrorCode.API_REQUEST_KEY_CAN_BE_REUSED_ONLY_WITH_THE_SAME_REQUEST,
                   result2.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void sameSalesWithDifferentApiRequestKey() {
        String apiRequestKey1 = "idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest request1 = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(apiRequestKey1).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result1 = gateway.transaction().sale(request1);
        assertTrue(result1.isSuccess());
        Transaction transaction1 = result1.getTarget();
        assertNotNull(transaction1.getId());

        String apiRequestKey2 = "idempotency-key-" + new Random().nextInt(1000000);
        TransactionRequest request2 = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(apiRequestKey2).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();
        Result<Transaction> result2 = gateway.transaction().sale(request2);
        assertTrue(result2.isSuccess());
        Transaction transaction2 = result2.getTarget();

        assertNotEquals(transaction1.getId(), transaction2.getId());
    }

    @Test
    public void saleWithApiRequestKeyFailsWhenApiRequestKeyIsTooBig() {
        TransactionRequest request1 = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(LONG_STRING_255).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result1 = gateway.transaction().sale(request1);
        assertTrue(result1.isSuccess());

        TransactionRequest request2 = new TransactionRequest().
            amount(new BigDecimal("200.00")).
            apiRequestKey(LONG_STRING_256).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> result2 = gateway.transaction().sale(request2);

        assertFalse(result2.isSuccess());
        assertNotNull(result2.getErrors());
        assertNotNull(result2.getErrors().getAllDeepValidationErrors());
        assertNotNull(result2.getErrors().getAllDeepValidationErrors().get(0));
        assertSame(ValidationErrorCode.API_REQUEST_KEY_TOO_LONG,
                   result2.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void submitForPartialSettlementWithApiRequestKeyReturnsOriginalOnDuplicateRequest() {
        String apiRequestKey = "partial-settlement-idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest saleRequest = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> saleResult = gateway.transaction().sale(saleRequest);
        assertTrue(saleResult.isSuccess());
        String transactionId = saleResult.getTarget().getId();

        BigDecimal partialAmount = new BigDecimal("50.00");
        TransactionRequest partialSettlementRequest = new TransactionRequest().
            amount(partialAmount).
            apiRequestKey(apiRequestKey);

        Result<Transaction> partialSettlementResult1 =
            gateway.transaction().submitForPartialSettlement(transactionId, partialSettlementRequest);
        assertTrue(partialSettlementResult1.isSuccess());
        Transaction partialSettlementTransaction1 = partialSettlementResult1.getTarget();
        assertEquals(partialAmount, partialSettlementTransaction1.getAmount());
        assertNotNull(partialSettlementTransaction1.getId());

        Result<Transaction> partialSettlementResult2 =
            gateway.transaction().submitForPartialSettlement(transactionId, partialSettlementRequest);
        assertTrue(partialSettlementResult2.isSuccess());
        Transaction partialSettlementTransaction2 = partialSettlementResult2.getTarget();

        assertEquals(partialSettlementTransaction1.getId(), partialSettlementTransaction2.getId());
        assertEquals(partialSettlementTransaction1.getAmount(), partialSettlementTransaction2.getAmount());
    }

    @Test
    public void submitForSettlementWithApiRequestKeyReturnsOriginalOnDuplicateRequest() {
        String apiRequestKey = "settlement-idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest saleRequest = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> saleResult = gateway.transaction().sale(saleRequest);
        assertTrue(saleResult.isSuccess());
        String transactionId = saleResult.getTarget().getId();
        BigDecimal originalAmount = saleResult.getTarget().getAmount();

        TransactionRequest settlementRequest = new TransactionRequest().
            apiRequestKey(apiRequestKey);

        Result<Transaction> settlementResult1 =
            gateway.transaction().submitForSettlement(transactionId, settlementRequest);
        assertTrue(settlementResult1.isSuccess());
        Transaction settlementTransaction1 = settlementResult1.getTarget();
        assertEquals(originalAmount, settlementTransaction1.getAmount());
        assertNotNull(settlementTransaction1.getId());

        Result<Transaction> settlementResult2 =
            gateway.transaction().submitForSettlement(transactionId, settlementRequest);
        assertTrue(settlementResult2.isSuccess());
        Transaction settlementTransaction2 = settlementResult2.getTarget();

        assertEquals(settlementTransaction1.getId(), settlementTransaction2.getId());
        assertEquals(settlementTransaction1.getAmount(), settlementTransaction2.getAmount());
    }

    @Test
    public void voidTransactionWithApiRequestKeyReturnsOriginalVoidOnDuplicateRequest() {
        String apiRequestKey = "void-idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest saleRequest = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> saleResult = gateway.transaction().sale(saleRequest);
        assertTrue(saleResult.isSuccess());
        String transactionId = saleResult.getTarget().getId();

        TransactionVoidRequest voidRequest = new TransactionVoidRequest().
            apiRequestKey(apiRequestKey);

        Result<Transaction> voidResult1 = gateway.transaction().voidTransaction(transactionId, voidRequest);
        assertTrue(voidResult1.isSuccess());
        Transaction voidedTransaction1 = voidResult1.getTarget();
        assertEquals(Transaction.Status.VOIDED, voidedTransaction1.getStatus());

        Result<Transaction> voidResult2 = gateway.transaction().voidTransaction(transactionId, voidRequest);
        assertTrue(voidResult2.isSuccess());
        Transaction voidedTransaction2 = voidResult2.getTarget();

        assertEquals(voidedTransaction1.getId(), voidedTransaction2.getId());
        assertEquals(voidedTransaction1.getStatus(), voidedTransaction2.getStatus());
        assertEquals(Transaction.Status.VOIDED, voidedTransaction2.getStatus());
    }

    @Test
    public void refundWithApiRequestKeyReturnsOriginalRefundOnDuplicateRequest() {
        String apiRequestKey = "refund-idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest saleRequest = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done().
            options().
            submitForSettlement(true).
            done();

        Result<Transaction> saleResult = gateway.transaction().sale(saleRequest);
        assertTrue(saleResult.isSuccess());
        String transactionId = saleResult.getTarget().getId();

        TestHelper.settle(gateway, transactionId);

        TransactionRefundRequest refundRequest = new TransactionRefundRequest().
            apiRequestKey(apiRequestKey);

        Result<Transaction> refundResult1 = gateway.transaction().refund(transactionId, refundRequest);
        assertTrue(refundResult1.isSuccess());
        Transaction refundTransaction1 = refundResult1.getTarget();
        assertEquals(Transaction.Type.CREDIT, refundTransaction1.getType());
        assertNotNull(refundTransaction1.getId());

        Result<Transaction> refundResult2 = gateway.transaction().refund(transactionId, refundRequest);
        assertTrue(refundResult2.isSuccess());
        Transaction refundTransaction2 = refundResult2.getTarget();

        assertEquals(refundTransaction1.getId(), refundTransaction2.getId());
        assertEquals(refundTransaction1.getType(), refundTransaction2.getType());
    }

    @Test
    public void creditWithApiRequestKeyReturnsOriginalOnDuplicateRequest() {
        String apiRequestKey = "credit-idempotency-key-" + new Random().nextInt(1000000);

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            apiRequestKey(apiRequestKey).
            creditCard().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationDate("05/2035").
            done();

        Result<Transaction> creditResult1 = gateway.transaction().credit(request);
        assertTrue(creditResult1.isSuccess());
        Transaction creditTransaction1 = creditResult1.getTarget();
        assertEquals(Transaction.Type.CREDIT, creditTransaction1.getType());
        assertNotNull(creditTransaction1.getId());

        Result<Transaction> creditResult2 = gateway.transaction().credit(request);
        assertTrue(creditResult2.isSuccess());
        Transaction creditTransaction2 = creditResult2.getTarget();

        assertEquals(creditTransaction1.getId(), creditTransaction2.getId());
        assertEquals(creditTransaction1.getType(), creditTransaction2.getType());
    }

}
