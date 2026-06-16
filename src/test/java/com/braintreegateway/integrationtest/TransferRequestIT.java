package com.braintreegateway.integrationtest;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferRequestIT extends IntegrationTest implements MerchantAccountTestConstants {

    @Test
    public void testTransactionWithTransferRequestOfWalletTransferTypeForAFTMerchant() throws ParseException {

        Calendar dateOfBirth = CalendarTestUtils.date("2002-04-10");

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            merchantAccountId("aft_first_data_wallet_transfer").
            creditCard().
                number("4111111111111111").
                expirationDate("06/2027").
                cvv("123").
            done().
            descriptor().
                name("companyname12*product1").
                phone("1232344444").
                url("example.com").
            done().
            billingAddress().
                firstName("Bob James").
                countryCodeAlpha2("CA").
                locality("Trois-Rivieres").
                region("QC").
                postalCode("G8Y 156").
                streetAddress("2346 Boul Lane").
            done().
            transfer()
                .type("wallet_transfer")
                .sender()
                    .firstName("Alice")
                    .middleName("A")
                    .lastName("Silva")
                    .accountReferenceNumber("1000012345")
                    .accountReferenceNumberType("BIC_SWIFT_CODE")
                    .address()
                        .streetAddress("1st Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                    .done()
                    .dateOfBirth(dateOfBirth)
                .done()
                .receiver()
                    .firstName("Bob")
                    .middleName("A")
                    .lastName("Souza")
                    .accountReferenceNumber("test@test.com")
                    .accountReferenceNumberType("EMAIL_ADDRESS")
                    .address()
                        .streetAddress("2nd Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                    .done()
                .done()
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

    @Test
    public void testTransactionNotCreatedForInvalidSenderAccountReferenceNumberType() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            merchantAccountId("aft_first_data_wallet_transfer").
            creditCard().
                number("4111111111111111").
                expirationDate("06/2027").
                cvv("123").
            done().
            transfer()
                .type("wallet_transfer")
                .sender()
                    .firstName("Bob")
                    .middleName("A")
                    .lastName("Souza")
                    .accountReferenceNumber("1000012345")
                    .accountReferenceNumberType("INVALID_ACCOUNT_REFERENCE_NUMBER_TYPE")
                    .address()
                        .streetAddress("2nd Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                    .done()
                .done()
            .done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_TRANSFER_SENDER_ACCOUNT_REFERENCE_NUMBER_TYPE_IS_INVALID,
            result.getErrors().forObject("accountFundingTransaction").onField("sender_account_reference_number_type").get(0).getCode()
        );
    }
    
    @Test
    public void testTransactionNotCreatedForInvalidReceiverAccountReferenceNumberType() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            merchantAccountId("aft_first_data_wallet_transfer").
            creditCard().
                number("4111111111111111").
                expirationDate("06/2027").
                cvv("123").
            done().
            transfer()
                .type("wallet_transfer")
                .receiver()
                    .firstName("Bob")
                    .middleName("A")
                    .lastName("Souza")
                    .accountReferenceNumber("1000012345")
                    .accountReferenceNumberType("INVALID_ACCOUNT_REFERENCE_NUMBER_TYPE")
                    .address()
                        .streetAddress("2nd Main Road")
                        .locality("Los Angeles")
                        .region("CA")
                        .countryCodeAlpha2("US")
                    .done()
                .done()
            .done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_TRANSFER_RECEIVER_ACCOUNT_REFERENCE_NUMBER_TYPE_IS_INVALID,
            result.getErrors().forObject("accountFundingTransaction").onField("receiver_account_reference_number_type").get(0).getCode()
        );
    }
}
