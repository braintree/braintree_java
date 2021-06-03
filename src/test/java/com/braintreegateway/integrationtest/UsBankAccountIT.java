package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;

import java.util.regex.Pattern;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountIT extends IntegrationTestNew {

    @Test
    public void findsUsBankAccountByToken() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = gateway.usBankAccount().find(result.getTarget().getToken());
        assertNotNull(usBankAccount);
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
        AchMandate achMandate = usBankAccount.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
        assertTrue(usBankAccount.isDefault());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenUsBankAccountIsMissing() {
        try {
            gateway.usBankAccount().find(TestHelper.generateInvalidUsBankAccountNonce());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void saleWithUsBankAccountByToken() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        TransactionRequest transactionRequest = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount);

        Result<Transaction> transactionResult = gateway.usBankAccount().sale(result.getTarget().getToken(), transactionRequest);
        assertTrue(result.isSuccess());
        Transaction transaction = transactionResult.getTarget();
        assertNotNull(transaction);

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
}
