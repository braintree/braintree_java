package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

public class SEPABankAccountIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "altpay_merchant", "altpay_merchant_public_key", "altpay_merchant_private_key");
    }

    @Test
    public void canExchangeNonceForSEPABankAccount() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateSEPABankAccountNonce(gateway, customer);
        PaymentMethodRequest request = new PaymentMethodRequest().
                customerId(customer.getId()).
                paymentMethodNonce(nonce);
        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());
        SEPABankAccount bankAccount =  (SEPABankAccount) gateway.paymentMethod().find(paymentMethod.getToken());
        assertEquals(paymentMethod.getToken(), bankAccount.getToken());
        assertEquals(bankAccount.getBic(), "DEUTDEFF");
        assertNotNull(bankAccount.getMaskedIban());
        assertNotNull(bankAccount.getMandateReferenceNumber());
        assertNotNull(bankAccount.getImageUrl());
    }
}
