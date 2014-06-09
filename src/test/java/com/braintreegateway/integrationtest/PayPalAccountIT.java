package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

public class PayPalAccountIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void findsPayPalAccountsByToken() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());


        PayPalAccount found = gateway.paypalAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenPayPalAccountIsMissing() {
        try {
            gateway.paypalAccount().find(" ");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findThrowsNotFoundExceptionWhenTokenPointsToCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest cardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard card = gateway.creditCard().create(cardRequest).getTarget();
        try {
            gateway.paypalAccount().find(card.getToken());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void deletePayPalAccount() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        Result<PayPalAccount> deleteResult = gateway.paypalAccount().delete(result.getTarget().getToken());
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void updateCanUpdateToken() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        String newToken = String.valueOf(new Random().nextInt());
        PayPalAccountRequest updateRequest = new PayPalAccountRequest().
            token(newToken);

        Result<PayPalAccount> updateResult = gateway.paypalAccount().update(result.getTarget().getToken(), updateRequest);
        assertTrue(updateResult.isSuccess());
        assertEquals(newToken, updateResult.getTarget().getToken());
    }

    @Test
    public void updateCanMakeDefault() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cardholderName("John Doe").
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        Result<CreditCard> creditCardRequest = gateway.creditCard().create(cardRequest);
        assertTrue(creditCardRequest.isSuccess());

        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        PayPalAccountRequest updateRequest = new PayPalAccountRequest().
            options().
              makeDefault(true).
              done();

        Result<PayPalAccount> updateResult = gateway.paypalAccount().update(result.getTarget().getToken(), updateRequest);
        assertTrue(updateResult.isSuccess());
        assertTrue(updateResult.getTarget().isDefault());
    }

    @Test
    public void updateMissingRaisesNotFoundError() {
        String newToken = String.valueOf(new Random().nextInt());
        PayPalAccountRequest updateRequest = new PayPalAccountRequest().
            token(newToken);

        try {
            gateway.paypalAccount().update("missing", updateRequest);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
}
