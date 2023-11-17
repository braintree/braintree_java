package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;

import java.util.regex.Pattern;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountIT extends IntegrationTest {

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
        AchMandate achMandate = usBankAccount.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
        assertNull(usBankAccount.getBusinessName());
        assertNotNull(usBankAccount.getCreatedAt());
        assertEquals(customer.getId(), usBankAccount.getCustomerId());
        assertNull(usBankAccount.getDescription());
        assertEquals("Dan", usBankAccount.getFirstName());
        assertNotNull(usBankAccount.getImageUrl());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("Schulman", usBankAccount.getLastName());
        assertNull(usBankAccount.getOwnerId());
        assertEquals("personal", usBankAccount.getOwnershipType());
        assertNull(usBankAccount.getPlaidVerifiedAt());
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertNotNull(usBankAccount.getSubscriptions());
        assertNotNull(usBankAccount.getToken());
        assertNotNull(usBankAccount.getUpdatedAt());
        assertTrue(usBankAccount.getVerifications().size() >= 1);
        assertTrue(usBankAccount.isDefault());
        assertFalse(usBankAccount.isVerifiable());
        assertTrue(usBankAccount.isVerified());
    }

    @Test
    public void findsUsBankAccountByTokenForBusiness() {
        String nonce = TestHelper.generateValidUsBankAccountNonceForBusiness(gateway);
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
        AchMandate achMandate = usBankAccount.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Big Tech", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
        assertEquals("Big Tech", usBankAccount.getBusinessName());
        assertNotNull(usBankAccount.getCreatedAt());
        assertEquals(customer.getId(), usBankAccount.getCustomerId());
        assertNull(usBankAccount.getDescription());
        assertNotNull(usBankAccount.getImageUrl());
        assertEquals("1234", usBankAccount.getLast4());
        assertNull(usBankAccount.getOwnerId());
        assertEquals("business", usBankAccount.getOwnershipType());
        assertNull(usBankAccount.getPlaidVerifiedAt());
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertEquals(0, usBankAccount.getSubscriptions().size());
        assertNotNull(usBankAccount.getToken());
        assertNotNull(usBankAccount.getUpdatedAt());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertTrue(usBankAccount.isDefault());
        assertFalse(usBankAccount.isVerifiable());
        assertTrue(usBankAccount.isVerified());
        assertEquals(usBankAccount.getCreatedAt(), usBankAccount.getUpdatedAt());
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
        AchMandate achMandate = usBankAccountDetails.getAchMandate();
        assertEquals("cl mandate text", achMandate.getText());
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccountDetails.getBankName()));
        assertNull(usBankAccountDetails.getBusinessName());
        assertEquals("Dan", usBankAccountDetails.getFirstName());
        assertNotNull(usBankAccountDetails.getGlobalId());
        assertNotNull(usBankAccountDetails.getImageUrl());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("Schulman", usBankAccountDetails.getLastName());
        assertEquals("personal", usBankAccountDetails.getOwnershipType());
        assertEquals("021000021", usBankAccountDetails.getRoutingNumber());
        assertNotNull(usBankAccountDetails.getToken());
        assertFalse(usBankAccountDetails.isVerified());
    }
}
