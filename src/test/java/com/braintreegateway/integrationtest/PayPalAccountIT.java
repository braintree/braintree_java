package com.braintreegateway.integrationtest;

import com.braintreegateway.test.Nonce;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.*;

public class PayPalAccountIT extends IntegrationTest {

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
        assertNotNull(result.getTarget().getImageUrl());


        PayPalAccount found = gateway.paypalAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNotNull(found.isDefault());
        assertNotNull(found.getEmail());
    }

    @Test
    public void findsBillingAgreementsPayPalAccountsByToken() {
        String nonce = TestHelper.generateBillingAgreementPayPalNonce(gateway);

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget().getImageUrl());

        PayPalAccount found = gateway.paypalAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNotNull(found.isDefault());
        assertNotNull(found.getEmail());
        assertNotNull(found.getBillingAgreementId());
    }

    @Test
    public void findsBillingAgreementsPayPalAccountsByTokenFromFakeNonce() {
        String nonce = Nonce.PayPalBillingAgreement;

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget().getImageUrl());

        PayPalAccount found = gateway.paypalAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
        assertNotNull(found.isDefault());
        assertNotNull(found.getEmail());
        assertNotNull(found.getBillingAgreementId());
    }

    @Test
    public void findReturnsSubscriptionsAssociatedWithAPaypalAccount() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest paypalRequest = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> paypalResult = gateway.paymentMethod().create(paypalRequest);
        assertTrue(paypalResult.isSuccess());

        PaymentMethod paymentMethod = paypalResult.getTarget();

        Plan plan = PlanFixture.PLAN_WITHOUT_TRIAL;
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest().
                paymentMethodToken(paymentMethod.getToken()).
                planId(plan.getId());

        Result<Subscription> subscriptionResult = gateway.subscription().create(subscriptionRequest);
        assertTrue(subscriptionResult.isSuccess());
        Subscription subscription1 = subscriptionResult.getTarget();
        subscriptionResult = gateway.subscription().create(subscriptionRequest);
        assertTrue(subscriptionResult.isSuccess());
        Subscription subscription2 = subscriptionResult.getTarget();

        PayPalAccount found = gateway.paypalAccount().find(paymentMethod.getToken());
        assertNotNull(found);
        assertTrue(found.getSubscriptions().contains(subscription1));
        assertTrue(found.getSubscriptions().contains(subscription2));
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
