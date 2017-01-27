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

public class UsBankAccountIT extends IntegrationTest {

    @Test
    public void findsUsBankAccountByToken() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

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
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        TransactionRequest transactionRequest = new TransactionRequest()
            .merchantAccountId("us_bank_merchant_account")
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
