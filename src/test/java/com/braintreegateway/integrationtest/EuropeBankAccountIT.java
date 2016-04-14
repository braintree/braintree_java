package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Test;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.*;

public class EuropeBankAccountIT extends IntegrationTest {

    @Test
    public void canExchangeNonceForEuropeBankAccount() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateEuropeBankAccountNonce(gateway, customer);
        PaymentMethodRequest request = new PaymentMethodRequest().
                customerId(customer.getId()).
                paymentMethodNonce(nonce);
        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        EuropeBankAccount bankAccount =  (EuropeBankAccount) gateway.paymentMethod().find(paymentMethod.getToken());
        assertEquals(paymentMethod.getToken(), bankAccount.getToken());
        assertEquals(bankAccount.getBic(), "DEUTDEFF");
        assertNotNull(bankAccount.getMaskedIban());
        assertNotNull(bankAccount.getMandateReferenceNumber());
        assertNotNull(bankAccount.getImageUrl());
    }

    @Test
    public void findReturnsNoSubscriptionsAssociatedWithEuropeBankAccount() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();
        String nonce = TestHelper.generateEuropeBankAccountNonce(gateway, customer);

        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());

        EuropeBankAccount bankAccount = (EuropeBankAccount) gateway.paymentMethod().find(paymentMethod.getToken());
        assertEquals(paymentMethod.getToken(), bankAccount.getToken());
        assertEquals(bankAccount.getBic(), "DEUTDEFF");
        assertNotNull(bankAccount.getMaskedIban());
        assertNotNull(bankAccount.getMandateReferenceNumber());
        assertNotNull(bankAccount.getImageUrl());

        assertEquals(bankAccount.getSubscriptions(), Collections.EMPTY_LIST);
    }
}
