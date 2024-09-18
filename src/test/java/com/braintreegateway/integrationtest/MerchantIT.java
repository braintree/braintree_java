package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MerchantIT extends IntegrationTest {
    private MerchantAccount getMerchantAccountForCurrency(Merchant merchant, String currency) {
        for (MerchantAccount merchantAccount : merchant.getMerchantAccounts()) {
            if (merchantAccount.getId().equals(currency)) {
                return merchantAccount;
            }
        }
        return null;
    }

    @BeforeEach
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );
    }

    @Test
    public void createMerchantTest() {
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("credit_card", "paypal")).
            scope("read_write,shared_vault_transactions");

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertEquals("name@email.com", result.getTarget().getEmail());
        assertEquals("name@email.com", result.getTarget().getCompanyName());
        assertEquals("GBR", result.getTarget().getCountryCodeAlpha3());
        assertEquals("GB", result.getTarget().getCountryCodeAlpha2());
        assertEquals("826", result.getTarget().getCountryCodeNumeric());
        assertEquals("United Kingdom", result.getTarget().getCountryName());

        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getCredentials().getRefreshToken().startsWith("refresh_token"));
        assertEquals(result.getTarget().getCredentials().getScope(), "read_write,shared_vault_transactions");
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());
    }

    @Test
    public void createFailsWithInvalidPaymentMethods() {
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("fake_money"));

        Result<Merchant> result = gateway.merchant().create(request);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.MERCHANT_PAYMENT_METHODS_ARE_INVALID, result.getErrors().forObject("merchant").onField("payment-methods").get(0).getCode());
    }

    @Test
    public void createWithPayPalOnly() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
);

        MerchantRequest request = new MerchantRequest().
            email("name2@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("paypal")).
            payPalAccount().
                clientId("paypal_client_id").
                clientSecret("paypal_client_secret").
                done();

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getRefreshToken().startsWith("refresh_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());
    }

    @Test
    public void payPalOnlyAccountCannotRunCreditCardTransactions() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name2@email.com").
            countryCodeAlpha3("GBR").
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

    @Test
    public void createMultiCurrencyEUMerchant() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal")).
            currencies(Arrays.asList("GBP", "USD"));

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertEquals("name@email.com", result.getTarget().getEmail());
        assertEquals("Ziarog LTD", result.getTarget().getCompanyName());
        assertEquals("GBR", result.getTarget().getCountryCodeAlpha3());
        assertEquals("GB", result.getTarget().getCountryCodeAlpha2());
        assertEquals("826", result.getTarget().getCountryCodeNumeric());
        assertEquals("United Kingdom", result.getTarget().getCountryName());

        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getCredentials().getRefreshToken().startsWith("refresh_token"));
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());

        assertEquals(2, result.getTarget().getMerchantAccounts().size());

        MerchantAccount usdMerchantAccount = getMerchantAccountForCurrency(result.getTarget(), "USD");
        assertNotNull(usdMerchantAccount);
        assertEquals("USD", usdMerchantAccount.getCurrencyIsoCode());
        assertFalse(usdMerchantAccount.isDefault());

        MerchantAccount gbpMerchantAccount = getMerchantAccountForCurrency(result.getTarget(), "GBP");
        assertNotNull(gbpMerchantAccount);
        assertEquals("GBP", gbpMerchantAccount.getCurrencyIsoCode());
        assertTrue(gbpMerchantAccount.isDefault());
    }

    @Test
    public void createPayPalOnlyMultiCurrencyMerchant() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("paypal")).
            currencies(Arrays.asList("GBP", "USD")).
            payPalAccount().
                clientId("paypal_client_id").
                clientSecret("paypal_client_secret").
                done();

        Result<Merchant> result = gateway.merchant().create(request);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getId() != null && !result.getTarget().getId().isEmpty());
        assertEquals("name@email.com", result.getTarget().getEmail());
        assertEquals("Ziarog LTD", result.getTarget().getCompanyName());
        assertEquals("GBR", result.getTarget().getCountryCodeAlpha3());
        assertEquals("GB", result.getTarget().getCountryCodeAlpha2());
        assertEquals("826", result.getTarget().getCountryCodeNumeric());
        assertEquals("United Kingdom", result.getTarget().getCountryName());

        assertTrue(result.getTarget().getCredentials().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getCredentials().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getCredentials().getRefreshToken().startsWith("refresh_token"));
        assertEquals("bearer", result.getTarget().getCredentials().getTokenType());

        assertEquals(2, result.getTarget().getMerchantAccounts().size());

        MerchantAccount usdMerchantAccount = getMerchantAccountForCurrency(result.getTarget(), "USD");
        assertNotNull(usdMerchantAccount);
        assertEquals("USD", usdMerchantAccount.getCurrencyIsoCode());
        assertFalse(usdMerchantAccount.isDefault());

        MerchantAccount gbpMerchantAccount = getMerchantAccountForCurrency(result.getTarget(), "GBP");
        assertNotNull(gbpMerchantAccount);
        assertEquals("GBP", gbpMerchantAccount.getCurrencyIsoCode());
        assertTrue(gbpMerchantAccount.isDefault());
    }

    @Test
    public void returnErrorIfInvalidCurrencyPassed() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("paypal")).
            currencies(Arrays.asList("GBP", "FAKE")).
            payPalAccount().
                clientId("paypal_client_id").
                clientSecret("paypal_client_secret").
                done();

        Result<Merchant> result = gateway.merchant().create(request);

        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.MERCHANT_CURRENCIES_ARE_INVALID,
            result.getErrors().forObject("merchant").onField("currencies").get(0).getCode()
        );
    }
}
