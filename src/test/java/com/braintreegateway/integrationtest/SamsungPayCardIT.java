package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.test.Nonce;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class SamsungPayCardIT extends IntegrationTest {

    @Test
    public void createWithPaymentMethodNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.SamsungPayVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        SamsungPayCard samsungPayCard = (SamsungPayCard) result.getTarget();
        assertNotNull(samsungPayCard.getBin());
        assertNotNull(samsungPayCard.getCardType());
        assertNotNull(samsungPayCard.getCardholderName());
        assertNotNull(samsungPayCard.getCommercial());
        assertNotNull(samsungPayCard.getCountryOfIssuance());
        assertNotNull(samsungPayCard.getCreatedAt());
        assertNotNull(samsungPayCard.getCustomerId());
        assertNotNull(samsungPayCard.getCustomerLocation());
        assertNotNull(samsungPayCard.getDebit());
        assertNotNull(samsungPayCard.isDefault());
        assertNotNull(samsungPayCard.getDurbinRegulated());
        assertNotNull(samsungPayCard.getExpirationDate());
        assertNotNull(samsungPayCard.getExpirationMonth());
        assertNotNull(samsungPayCard.getExpirationYear());
        assertNotNull(samsungPayCard.isExpired());
        assertNotNull(samsungPayCard.getHealthcare());
        assertNotNull(samsungPayCard.getImageUrl());
        assertNotNull(samsungPayCard.getIssuingBank());
        assertNotNull(samsungPayCard.getLast4());
        assertNotNull(samsungPayCard.getMaskedNumber());
        assertNotNull(samsungPayCard.getPayroll());
        assertNotNull(samsungPayCard.getPrepaid());
        assertNotNull(samsungPayCard.getProductId());
        assertNotNull(samsungPayCard.getSubscriptions());
        assertNotNull(samsungPayCard.getToken());
        assertNotNull(samsungPayCard.getUniqueNumberIdentifier());
        assertNotNull(samsungPayCard.getUpdatedAt());

        SamsungPayCard foundSamsungPayCard = (SamsungPayCard) gateway.paymentMethod().find(samsungPayCard.getToken());
        assertEquals(samsungPayCard.getToken(), foundSamsungPayCard.getToken());
    }

    @Test
    public void searchOnPaymentInstrumentTypeIsSamsungPayCard() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            paymentMethodNonce(Nonce.SamsungPayVisa);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paymentInstrumentType().is(PaymentInstrumentType.SAMSUNG_PAY_CARD);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(PaymentInstrumentType.SAMSUNG_PAY_CARD, collection.getFirst().getPaymentInstrumentType());

        SamsungPayCardDetails samsungPayCardDetails = collection.getFirst().getSamsungPayCardDetails();
        assertNotNull(samsungPayCardDetails);
    }

    @Test
    public void createCustomerWithSamsungPayCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.SamsungPayVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(1, foundCustomer.getSamsungPayCards().size());

        SamsungPayCard samsungPayCard = foundCustomer.getSamsungPayCards().get(0);
        assertTrue(foundCustomer.getPaymentMethods().contains(samsungPayCard));
    }

    @Test
    public void saleWithSamsungPayAndVault() {
        String samsungPayNonce = Nonce.SamsungPayAmEx;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(samsungPayNonce).
            options().
                storeInVault(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.SAMSUNG_PAY_CARD, transaction.getPaymentInstrumentType());

        SamsungPayCardDetails samsungPayCardDetails = transaction.getSamsungPayCardDetails();

        assertNotNull(samsungPayCardDetails.getBin());
        assertNotNull(samsungPayCardDetails.getCardType());
        assertNotNull(samsungPayCardDetails.getCardholderName());
        assertNotNull(samsungPayCardDetails.getCommercial());
        assertNotNull(samsungPayCardDetails.getCountryOfIssuance());
        assertNotNull(samsungPayCardDetails.getDebit());
        assertNotNull(samsungPayCardDetails.getDurbinRegulated());
        assertNotNull(samsungPayCardDetails.getExpirationDate());
        assertNotNull(samsungPayCardDetails.getExpirationMonth());
        assertNotNull(samsungPayCardDetails.getExpirationYear());
        assertNotNull(samsungPayCardDetails.getHealthcare());
        assertNotNull(samsungPayCardDetails.getImageUrl());
        assertNotNull(samsungPayCardDetails.getIssuingBank());
        assertNotNull(samsungPayCardDetails.getLast4());
        assertNotNull(samsungPayCardDetails.getMaskedNumber());
        assertNotNull(samsungPayCardDetails.getPayroll());
        assertNotNull(samsungPayCardDetails.getPrepaid());
        assertNotNull(samsungPayCardDetails.getProductId());
        assertNotNull(samsungPayCardDetails.getToken());
    }
}
