package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class MerchantIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
    }

    @Test
    public void createMerchantTest() {
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("USA").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertEquals("name@email.com", result.getTarget().getEmail());
        assertEquals("name@email.com", result.getTarget().getCompanyName());
        assertEquals("USA", result.getTarget().getCountryCodeAlpha3());
        assertEquals("US", result.getTarget().getCountryCodeAlpha2());
        assertEquals("840", result.getTarget().getCountryCodeNumeric());
        assertEquals("United States of America", result.getTarget().getCountryName());

        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getCredentials().getRefreshToken() == null || result.getTarget().getCredentials().getRefreshToken().isEmpty());
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());
    }

    @Test
    public void createFailsWithInvalidPaymentMethods() {
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("USA").
            paymentMethods(Arrays.asList("fake_money"));

        Result<Merchant> result = gateway.merchant().create(request);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.MERCHANT_PAYMENT_METHODS_ARE_INVALID, result.getErrors().forObject("merchant").onField("payment-methods").get(0).getCode());
    }

    @Test
    public void createWithPayPalOnly() {
        this.gateway = new BraintreeGateway("client_id$development$signup_client_id", "client_secret$development$signup_client_secret");

        MerchantRequest request = new MerchantRequest().
            email("name2@email.com").
            countryCodeAlpha3("USA").
            paymentMethods(Arrays.asList("paypal")).
            payPalAccount().
                clientId("paypal_client_id").
                clientSecret("paypal_client_secret").
                done();

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getCredentials().getRefreshToken() == null || result.getTarget().getCredentials().getRefreshToken().isEmpty());
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());
    }

    @Test
    public void payPalOnlyAccountCannotRunCreditCardTransactions() {
        this.gateway = new BraintreeGateway("client_id$development$signup_client_id", "client_secret$development$signup_client_secret");

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name2@email.com").
            countryCodeAlpha3("USA").
            paymentMethods(Arrays.asList("paypal")).
            payPalAccount().
                clientId("paypal_client_id").
                clientSecret("paypal_client_secret").
                done();

        Result<Merchant> result = gateway.merchant().create(merchantRequest);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());

        BraintreeGateway gateway = new BraintreeGateway(result.getTarget().getCredentials().getAccessToken());
        TransactionRequest transactionRequest = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> transactionResult = gateway.transaction().sale(transactionRequest);

        assertTrue(transactionResult.getMessage().contains("Merchant account does not support payment instrument."));
        assertFalse(transactionResult.isSuccess());
    }
}
