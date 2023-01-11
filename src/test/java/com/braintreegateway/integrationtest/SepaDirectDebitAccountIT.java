package com.braintreegateway.integrationtest;

import com.braintreegateway.test.Nonce;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SepaDirectDebitAccountIT extends IntegrationTest {

    @Test
    public void findsSepaDirectDebitAccountsByToken() {
        String nonce = TestHelper.generateSepaDebitNonce(gateway);

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget().getImageUrl());

        SepaDirectDebitAccount found = gateway.sepaDirectDebitAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.isDefault());
        assertNotNull(found.getBankReferenceToken());
        assertNotNull(found.getMandateType().name());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.getMerchantOrPartnerCustomerId());
        assertNotNull(found.getGlobalId());
        assertNotNull(found.getCustomerGlobalId());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
    }

    @Test
    public void findsSepaDirectDebitAccountsByFakeNonce() {
        String nonce = Nonce.SepaDebit;

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget().getImageUrl());

        SepaDirectDebitAccount found = gateway.sepaDirectDebitAccount().find(result.getTarget().getToken());
        assertNotNull(found);
        assertEquals(found.getToken(), result.getTarget().getToken());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.isDefault());
        assertNotNull(found.getBankReferenceToken());
        assertNotNull(found.getMandateType().name());
        assertNotNull(found.getImageUrl());
        assertNotNull(found.getMerchantOrPartnerCustomerId());
        assertNotNull(found.getGlobalId());
        assertNotNull(found.getCustomerGlobalId());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getUpdatedAt());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenSepaDebitAccountIsMissing() {
        try {
            gateway.sepaDirectDebitAccount().find(" ");
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
            gateway.sepaDirectDebitAccount().find(card.getToken());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void deleteSepaDebitAccount() {
        String nonce = TestHelper.generateSepaDebitNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        String token = result.getTarget().getToken();
        assertTrue(result.isSuccess());

        Result<SepaDirectDebitAccount> deleteResult = gateway.sepaDirectDebitAccount().delete(token);
        assertTrue(deleteResult.isSuccess());
        try {
            gateway.sepaDirectDebitAccount().find(token);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
}
