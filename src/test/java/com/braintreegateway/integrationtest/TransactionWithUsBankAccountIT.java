package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;

import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionWithUsBankAccountIT extends IntegrationTest implements MerchantAccountTestConstants {
    @Test
    public void saleWithUsBankAccountNonce() {
        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateValidUsBankAccountNonce(gateway))
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.SETTLEMENT_PENDING, transaction.getStatus());

        UsBankAccountDetails usBankAccountDetails = transaction.getUsBankAccountDetails();
        assertEquals("021000021", usBankAccountDetails.getRoutingNumber());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccountDetails.getBankName()));
        AchMandate achMandate = usBankAccountDetails.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
    }

    @Test
    public void saleWithUsBankAccountNonceAndVaultedToken() {
        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateValidUsBankAccountNonce(gateway))
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.SETTLEMENT_PENDING, transaction.getStatus());

        UsBankAccountDetails usBankAccountDetails = transaction.getUsBankAccountDetails();
        assertEquals("021000021", usBankAccountDetails.getRoutingNumber());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccountDetails.getBankName()));
        AchMandate achMandate = usBankAccountDetails.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());

        request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodToken(transaction.getUsBankAccountDetails().getToken())
            .options()
                .submitForSettlement(true)
                .done();

        result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.SETTLEMENT_PENDING, transaction.getStatus());

        usBankAccountDetails = transaction.getUsBankAccountDetails();
        assertEquals("021000021", usBankAccountDetails.getRoutingNumber());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccountDetails.getBankName()));
        achMandate = usBankAccountDetails.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
    }

    @Test
    public void saleWithInvalidUsBankAccountNonce() {
        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateInvalidUsBankAccountNonce())
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_PAYMENT_METHOD_NONCE_UNKNOWN,
                result.getErrors().forObject("transaction").onField("paymentMethodNonce").get(0).getCode());

    }

    @Test
    public void exemptMerchantChargeNonPlaidNonce() {
        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateValidUsBankAccountNonce(gateway))
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = gateway.usBankAccount().find(
            result.getTarget().getUsBankAccountDetails().getToken()
        );

        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK, verification.getVerificationMethod());
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
    }

    @Test
    public void compliantMerchantChargeNonPlaidNonce() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateValidUsBankAccountNonce(gateway))
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_US_BANK_ACCOUNT_NONCE_MUST_BE_PLAID_VERIFIED,
                result.getErrors().forObject("transaction").onField("paymentMethodNonce").get(0).getCode());
    }

    // Ignoring this test until we have a more stable CI env
    @Disabled
    @Test
    public void compliantMerchantChargePlaidNonce() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generatePlaidUsBankAccountNonce(gateway))
            .options()
                .submitForSettlement(true)
                .storeInVault(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = gateway.usBankAccount().find(
            result.getTarget().getUsBankAccountDetails().getToken()
        );

        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.TOKENIZED_CHECK, verification.getVerificationMethod());
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
    }

    @Test
    public void compliantMerchantChargeUnverifiedBankAccount() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(paymentMethodRequest);
        assertTrue(paymentMethodResult.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();

        assertEquals(0, usBankAccount.getVerifications().size());
        assertEquals(false, usBankAccount.isVerified());

        TransactionRequest transactionRequest = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodToken(usBankAccount.getToken())
            .options()
                .submitForSettlement(true)
            .done();

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_US_BANK_ACCOUNT_NOT_VERIFIED,
                result.getErrors().forObject("transaction").onField("paymentMethodToken").get(0).getCode());
    }
}
