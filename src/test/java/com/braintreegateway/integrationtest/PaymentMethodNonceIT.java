package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentMethodNonceIT extends IntegrationTest {

    @Test
    public void createFromExistingPaymentMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        PaymentMethod paymentMethod = paymentMethodResult.getTarget();

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(paymentMethod.getToken());
        assertTrue(result.isSuccess());

        PaymentMethodNonce newNonce = result.getTarget();
        assertNotNull(newNonce);
        assertNotNull(newNonce.getNonce());
    }

    @Test
    public void createRaisesIfNotFound() {
        try {
            gateway.paymentMethodNonce().create("not-a-token");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findReturnsPaymentMethodNonce() {
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateThreeDSecureNonce(gateway, creditCardRequest);

        PaymentMethodNonce foundNonce = gateway.paymentMethodNonce().find(nonce);

        assertEquals(nonce, foundNonce.getNonce());
        assertTrue(foundNonce.getThreeDSecureInfo().isLiabilityShifted());
    }

    @Test
    public void findReturnsNull3DSDetailsIfNotPresent() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);

        PaymentMethodNonce foundNonce = gateway.paymentMethodNonce().find(nonce);

        assertNull(foundNonce.getThreeDSecureInfo());
    }
    @Test
    public void findRaisesIfNotFound() {
        try {
            gateway.paymentMethodNonce().find("not-a-nonce");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
}
