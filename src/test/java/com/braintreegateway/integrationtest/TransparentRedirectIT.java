package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransparentRedirectIT extends IntegrationTest implements MerchantAccountTestConstants {

    @Test
    public void createTransactionFromTransparentRedirect() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
                storeInVault(true).
                done();

        TransactionRequest trParams = new TransactionRequest().
            type(Transaction.Type.SALE);

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);
        assertTrue(result.isSuccess());
        assertEquals(CreditCardNumber.VISA.number.substring(0, 6), result.getTarget().getCreditCard().getBin());
        assertEquals(TransactionAmount.AUTHORIZE.amount, result.getTarget().getAmount());
    }

    @Test
    public void createTransactionFromTransparentRedirectSpecifyingMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        TransactionRequest trParams = new TransactionRequest().
            type(Transaction.Type.SALE).
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID);

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);

        assertTrue(result.isSuccess());
        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, result.getTarget().getMerchantAccountId());
    }

    @Test
    public void createTransactionFromTransparentRedirectSpecifyingDescriptor() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        TransactionRequest trParams = new TransactionRequest().
            type(Transaction.Type.SALE).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals("123*123456789012345678", transaction.getDescriptor().getName());
        assertEquals("3334445555", transaction.getDescriptor().getPhone());
    }

    @Test
    public void createTransactionFromTransparentRedirectSpecifyingLevel2Attributes() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        TransactionRequest trParams = new TransactionRequest().
            type(Transaction.Type.SALE).
            taxAmount(new BigDecimal("10.00")).
            taxExempt(true).
            purchaseOrderNumber("12345");

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals(new BigDecimal("10.00"), transaction.getTaxAmount());
        assertTrue(transaction.isTaxExempt());
        assertEquals("12345", transaction.getPurchaseOrderNumber());
    }

    @Test
    public void createCustomerFromTransparentRedirect() {
        CustomerRequest request = new CustomerRequest().firstName("John");
        CustomerRequest trParams = new CustomerRequest().lastName("Doe");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertTrue(result.isSuccess());
        assertEquals("John", result.getTarget().getFirstName());
        assertEquals("Doe", result.getTarget().getLastName());
    }

    @Test
    public void updateCustomerFromTransparentRedirect() {
        CustomerRequest request = new CustomerRequest().
            firstName("John").
            lastName("Doe");
        Customer customer = gateway.customer().create(request).getTarget();

        CustomerRequest updateRequest = new CustomerRequest().firstName("Jane");
        CustomerRequest trParams = new CustomerRequest().customerId(customer.getId()).lastName("Dough");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());

        Result<Customer> result = gateway.transparentRedirect().confirmCustomer(queryString);

        assertTrue(result.isSuccess());
        Customer updatedCustomer = gateway.customer().find(customer.getId());
        assertEquals("Jane", updatedCustomer.getFirstName());
        assertEquals("Dough", updatedCustomer.getLastName());
    }

    @Test
    public void createCreditCardFromTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            customerId(customer.getId()).
            number("4111111111111111").
            expirationDate("10/10");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());

        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);

        assertTrue(result.isSuccess());
        assertEquals("411111", result.getTarget().getBin());
        assertEquals("1111", result.getTarget().getLast4());
        assertEquals("10/2010", result.getTarget().getExpirationDate());
    }

    @Test
    public void updateCreditCardFromTransparentRedirect() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest request = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(request).getTarget();

        CreditCardRequest updateRequest = new CreditCardRequest();
        CreditCardRequest trParams = new CreditCardRequest().
            paymentMethodToken(card.getToken()).
            number("4111111111111111").
            expirationDate("10/10");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, updateRequest, gateway.transparentRedirect().url());

        Result<CreditCard> result = gateway.transparentRedirect().confirmCreditCard(queryString);

        assertTrue(result.isSuccess());
        CreditCard updatedCreditCard = gateway.creditCard().find(card.getToken());
        assertEquals("411111", updatedCreditCard.getBin());
        assertEquals("1111", updatedCreditCard.getLast4());
        assertEquals("10/2010", updatedCreditCard.getExpirationDate());
    }

    @Test
    public void errorRaisedWhenConfirmingIncorrectResource() {
        CustomerRequest request = new CustomerRequest().firstName("John");
        CustomerRequest trParams = new CustomerRequest().lastName("Doe");
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());

        try {
            gateway.transparentRedirect().confirmTransaction(queryString);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("You attemped to confirm a transaction, but received a customer.", e.getMessage());
        }
    }

    @Test
    public void errorNotRaisedWhenReceivingApiErrorResponse() {
        TransactionRequest invalidRequest = new TransactionRequest();
        TransactionRequest trParams = new TransactionRequest().type(Transaction.Type.SALE);
        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, invalidRequest, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrors().deepSize() > 0);
    }
}
