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

public class MasterpassCardIT extends IntegrationTest {

    @Test
    public void createWithPaymentMethodNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.MasterpassVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        MasterpassCard masterpassCard = (MasterpassCard) result.getTarget();
        assertNotNull(masterpassCard.getBillingAddress());
        assertNotNull(masterpassCard.getBin());
        assertNotNull(masterpassCard.getCardType());
        assertNotNull(masterpassCard.getCardholderName());
        assertNotNull(masterpassCard.getCommercial());
        assertNotNull(masterpassCard.getCountryOfIssuance());
        assertNotNull(masterpassCard.getCreatedAt());
        assertNotNull(masterpassCard.getCustomerId());
        assertNotNull(masterpassCard.getCustomerLocation());
        assertNotNull(masterpassCard.getDebit());
        assertNotNull(masterpassCard.isDefault());
        assertNotNull(masterpassCard.getDurbinRegulated());
        assertNotNull(masterpassCard.getExpirationDate());
        assertNotNull(masterpassCard.getExpirationMonth());
        assertNotNull(masterpassCard.getExpirationYear());
        assertNotNull(masterpassCard.isExpired());
        assertNotNull(masterpassCard.getHealthcare());
        assertNotNull(masterpassCard.getImageUrl());
        assertNotNull(masterpassCard.getIssuingBank());
        assertNotNull(masterpassCard.getLast4());
        assertNotNull(masterpassCard.getMaskedNumber());
        assertNotNull(masterpassCard.getPayroll());
        assertNotNull(masterpassCard.getPrepaid());
        assertNotNull(masterpassCard.getProductId());
        assertNotNull(masterpassCard.getSubscriptions());
        assertNotNull(masterpassCard.getToken());
        assertNotNull(masterpassCard.getUniqueNumberIdentifier());
        assertNotNull(masterpassCard.getUpdatedAt());

        MasterpassCard foundMasterpassCard = (MasterpassCard) gateway.paymentMethod().find(masterpassCard.getToken());
        assertEquals(masterpassCard.getToken(), foundMasterpassCard.getToken());
    }

    @Test
    public void searchOnPaymentInstrumentTypeIsMasterpassCard() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            paymentMethodNonce(Nonce.MasterpassVisa);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paymentInstrumentType().is(PaymentInstrumentType.MASTERPASS_CARD);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(PaymentInstrumentType.MASTERPASS_CARD, collection.getFirst().getPaymentInstrumentType());

        MasterpassCardDetails masterpassCardDetails = collection.getFirst().getMasterpassCardDetails();
        assertNotNull(masterpassCardDetails);
    }

    @Test
    public void createCustomerWithMasterpassCard() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(Nonce.MasterpassVisa);

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        Customer foundCustomer = gateway.customer().find(customer.getId());
        assertEquals(1, foundCustomer.getMasterpassCards().size());

        MasterpassCard masterpassCard = foundCustomer.getMasterpassCards().get(0);
        assertTrue(foundCustomer.getPaymentMethods().contains(masterpassCard));
    }

    @Test
    public void saleWithMasterpassAndVault() {
        String masterpassNonce = Nonce.MasterpassAmEx;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(masterpassNonce).
            options().
                storeInVault(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.MASTERPASS_CARD, transaction.getPaymentInstrumentType());

        MasterpassCardDetails masterpassCardDetails = transaction.getMasterpassCardDetails();

        assertNotNull(masterpassCardDetails.getBin());
        assertNotNull(masterpassCardDetails.getCardType());
        assertNotNull(masterpassCardDetails.getCardholderName());
        assertNotNull(masterpassCardDetails.getCommercial());
        assertNotNull(masterpassCardDetails.getCountryOfIssuance());
        assertNotNull(masterpassCardDetails.getDebit());
        assertNotNull(masterpassCardDetails.getDurbinRegulated());
        assertNotNull(masterpassCardDetails.getExpirationDate());
        assertNotNull(masterpassCardDetails.getExpirationMonth());
        assertNotNull(masterpassCardDetails.getExpirationYear());
        assertNotNull(masterpassCardDetails.getHealthcare());
        assertNotNull(masterpassCardDetails.getImageUrl());
        assertNotNull(masterpassCardDetails.getIssuingBank());
        assertNotNull(masterpassCardDetails.getLast4());
        assertNotNull(masterpassCardDetails.getMaskedNumber());
        assertNotNull(masterpassCardDetails.getPayroll());
        assertNotNull(masterpassCardDetails.getPrepaid());
        assertNotNull(masterpassCardDetails.getProductId());
        assertNotNull(masterpassCardDetails.getToken());
    }
}
