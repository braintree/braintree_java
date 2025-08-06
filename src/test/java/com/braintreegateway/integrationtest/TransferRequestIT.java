package com.braintreegateway.integrationtest;

import java.math.BigDecimal;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferRequestIT extends IntegrationTest implements MerchantAccountTestConstants {

    @Test
    public void testTransactionWithTransferRequestForWalletTransferTypeForAFTMerchant() {
        TransactionRequest request = new TransactionRequest().
           amount(new BigDecimal("100.00")).
           merchantAccountId("aft_first_data_wallet_transfer").
           creditCard().
              number("4111111111111111").
              expirationDate("06/2026").
               cvv("123").
               done().
           transfer()
              .type("wallet_transfer")
              .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertTrue(transaction.isAccountFundingTransaction());
    }

    @Test
    public void testTransactionNotCreatedForInvalidTransferType() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            merchantAccountId("aft_first_data_wallet_transfer").
            creditCard().
                number("4111111111111111").
                expirationDate("06/2026").
                cvv("123").
                done().
            transfer().
                type("invalid_transfer").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
    }
}
