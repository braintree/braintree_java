package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentMethodIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void createPayPalAccountWithNonce() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());
        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());

        PayPalAccount paypalAccount = (PayPalAccount) paymentMethod;
        assertNotNull(paypalAccount.getEmail());
    }
}
