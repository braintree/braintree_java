package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.test.Nonce;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisaCheckoutCardIT extends IntegrationTest {

    @Test
    public void createWithPaymentMethodNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.VisaCheckoutVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        VisaCheckoutCard visaCheckoutCard = (VisaCheckoutCard) result.getTarget();
        assertEquals("abc123", visaCheckoutCard.getCallId());
        assertNotNull(visaCheckoutCard.getBillingAddress());
        assertNotNull(visaCheckoutCard.getBin());
        assertNotNull(visaCheckoutCard.getBusiness());
        assertNotNull(visaCheckoutCard.getCardType());
        assertNotNull(visaCheckoutCard.getCardholderName());
        assertNotNull(visaCheckoutCard.getCommercial());
        assertNotNull(visaCheckoutCard.getConsumer());
        assertNotNull(visaCheckoutCard.getCorporate());
        assertNotNull(visaCheckoutCard.getCountryOfIssuance());
        assertNotNull(visaCheckoutCard.getCreatedAt());
        assertNotNull(visaCheckoutCard.getCustomerId());
        assertNotNull(visaCheckoutCard.getCustomerLocation());
        assertNotNull(visaCheckoutCard.getDebit());
        assertNotNull(visaCheckoutCard.isDefault());
        assertNotNull(visaCheckoutCard.getDurbinRegulated());
        assertNotNull(visaCheckoutCard.getExpirationDate());
        assertNotNull(visaCheckoutCard.getExpirationMonth());
        assertNotNull(visaCheckoutCard.getExpirationYear());
        assertNotNull(visaCheckoutCard.isExpired());
        assertNotNull(visaCheckoutCard.getHealthcare());
        assertNotNull(visaCheckoutCard.getImageUrl());
        assertNotNull(visaCheckoutCard.getIssuingBank());
        assertNotNull(visaCheckoutCard.getLast4());
        assertNotNull(visaCheckoutCard.getMaskedNumber());
        assertNotNull(visaCheckoutCard.getPayroll());
        assertNotNull(visaCheckoutCard.getPrepaid());
        assertNotNull(visaCheckoutCard.getPrepaidReloadable());
        assertNotNull(visaCheckoutCard.getProductId());
        assertNotNull(visaCheckoutCard.getPurchase());
        assertNotNull(visaCheckoutCard.getSubscriptions());
        assertNotNull(visaCheckoutCard.getToken());
        assertNotNull(visaCheckoutCard.getUniqueNumberIdentifier());
        assertNotNull(visaCheckoutCard.getUpdatedAt());

        VisaCheckoutCard foundVisaCheckoutCard = (VisaCheckoutCard) gateway.paymentMethod().find(visaCheckoutCard.getToken());
        assertEquals(visaCheckoutCard.getToken(), foundVisaCheckoutCard.getToken());
    }

    @Test
    public void createWithPaymentMethodNonceWithVerification() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.VisaCheckoutVisa).
            options().
                verifyCard(true).
                verificationAmount("1.11").
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        VisaCheckoutCard visaCheckoutCard = (VisaCheckoutCard) result.getTarget();
        assertNotNull(visaCheckoutCard);

        CreditCardVerification verification = visaCheckoutCard.getVerification();
        assertNotNull(verification);
        assertNotNull(verification.getStatus());
    }

    @Test
    public void searchOnPaymentInstrumentTypeIsVisaCheckoutCard() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            paymentMethodNonce(Nonce.VisaCheckoutVisa);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paymentInstrumentType().is(PaymentInstrumentType.VISA_CHECKOUT_CARD);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(PaymentInstrumentType.VISA_CHECKOUT_CARD, collection.getFirst().getPaymentInstrumentType());

        VisaCheckoutCardDetails visaCheckoutCardDetails = collection.getFirst().getVisaCheckoutCardDetails();
        assertNotNull(visaCheckoutCardDetails);
    }

    @Test
    public void createCustomerWithVisaCheckoutCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.VisaCheckoutVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(1, foundCustomer.getVisaCheckoutCards().size());

        VisaCheckoutCard visaCheckoutCard = foundCustomer.getVisaCheckoutCards().get(0);
        assertTrue(foundCustomer.getPaymentMethods().contains(visaCheckoutCard));
    }

    @Test
    public void saleWithVisaCheckoutAndVault() {
        String visaCheckoutNonce = Nonce.VisaCheckoutAmEx;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(visaCheckoutNonce).
            options().
                storeInVault(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.VISA_CHECKOUT_CARD, transaction.getPaymentInstrumentType());

        VisaCheckoutCardDetails visaCheckoutCardDetails = transaction.getVisaCheckoutCardDetails();

        assertEquals("abc123", visaCheckoutCardDetails.getCallId());
        assertNotNull(visaCheckoutCardDetails.getBin());
        assertNotNull(visaCheckoutCardDetails.getBusiness());
        assertNotNull(visaCheckoutCardDetails.getCardType());
        assertNotNull(visaCheckoutCardDetails.getCardholderName());
        assertNotNull(visaCheckoutCardDetails.getCommercial());
        assertNotNull(visaCheckoutCardDetails.getConsumer());
        assertNotNull(visaCheckoutCardDetails.getCorporate());
        assertNotNull(visaCheckoutCardDetails.getCountryOfIssuance());
        assertNotNull(visaCheckoutCardDetails.getDebit());
        assertNotNull(visaCheckoutCardDetails.getDurbinRegulated());
        assertNotNull(visaCheckoutCardDetails.getExpirationDate());
        assertNotNull(visaCheckoutCardDetails.getExpirationMonth());
        assertNotNull(visaCheckoutCardDetails.getExpirationYear());
        assertNotNull(visaCheckoutCardDetails.getHealthcare());
        assertNotNull(visaCheckoutCardDetails.getImageUrl());
        assertNotNull(visaCheckoutCardDetails.getIssuingBank());
        assertNotNull(visaCheckoutCardDetails.getLast4());
        assertNotNull(visaCheckoutCardDetails.getMaskedNumber());
        assertNotNull(visaCheckoutCardDetails.getPayroll());
        assertNotNull(visaCheckoutCardDetails.getPrepaid());
        assertNotNull(visaCheckoutCardDetails.getPrepaidReloadable());
        assertNotNull(visaCheckoutCardDetails.getProductId());
        assertNotNull(visaCheckoutCardDetails.getPurchase());
        assertNotNull(visaCheckoutCardDetails.getToken());
    }
}
