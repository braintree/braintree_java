package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.testhelpers.TestHelper;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MerchantAccountIT extends IntegrationTest {

    @Test
    public void findThrowsExceptionIfMerchantNotFound() {
        try {
            gateway.merchantAccount().find("non-existent");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
    
    @Test
    public void retrievesCurrencyIsoCode() {
        MerchantAccount ma = gateway.merchantAccount().find("sandbox_master_merchant_account");

        assertEquals("USD", ma.getCurrencyIsoCode());
    }

    @Test
    public void createMerchantAccountForCurrency() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(merchantRequest);
        assertTrue(merchantResult.isSuccess());

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("USD").
            id("customId");

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertTrue(result.isSuccess());
        assertEquals("customId", result.getTarget().getId());
        assertEquals("USD", result.getTarget().getCurrencyIsoCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesMerchantAccountExistsForCurrency() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(merchantRequest);
        assertTrue(merchantResult.isSuccess());

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("GBP");

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("currency");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_MERCHANT_ACCOUNT_EXISTS_FOR_CURRENCY, errors.get(0).getCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesCurrencyIsInvalid() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(merchantRequest);
        assertTrue(merchantResult.isSuccess());

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest();

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("currency");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_CURRENCY_IS_REQUIRED, errors.get(0).getCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesCurrencyIsRequired() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(merchantRequest);
        assertTrue(merchantResult.isSuccess());

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("FAKE_CURRENCY");

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("currency");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_CURRENCY_IS_INVALID, errors.get(0).getCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesMerchantAccountExistsForToken() {
        this.gateway = new BraintreeGateway(
            "client_id$development$integration_client_id",
            "client_secret$development$integration_client_secret"
        );

        MerchantRequest merchantRequest = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            companyName("Ziarog LTD").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(merchantRequest);
        assertTrue(merchantResult.isSuccess());

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("USD").
            id(merchantResult.getTarget().getMerchantAccounts().get(0).getId());

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("id");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_MERCHANT_ACCOUNT_EXISTS_FOR_ID, errors.get(0).getCode());
    }

    @Test
    public void returnAllMerchantAccounts() {
        this.gateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");

        String code = TestHelper.createOAuthGrant(this.gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> accessTokenResult = this.gateway.oauth().createTokenFromCode(oauthRequest);

        BraintreeGateway gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        PaginatedCollection<MerchantAccount> result = gateway.merchantAccount().all();
        List<MerchantAccount> merchantAccounts = new ArrayList<MerchantAccount>();
        for (MerchantAccount merchantAccount : result) {
            merchantAccounts.add(merchantAccount);
        }

        assertTrue(merchantAccounts.size() > 20);
    }

    @Test
    public void returnsMerchantAccountWithCorrectAttributes() {
        this.gateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("GBR").
            paymentMethods(Arrays.asList("credit_card", "paypal")).
            scope("read_write,shared_vault_transactions");

        Result<Merchant> merchantResult = gateway.merchant().create(request);

        this.gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        PaginatedCollection<MerchantAccount> result = gateway.merchantAccount().all();
        List<MerchantAccount> merchantAccounts = new ArrayList<MerchantAccount>();
        for (MerchantAccount merchantAccount : result) {
            merchantAccounts.add(merchantAccount);
        }

        assertEquals(merchantAccounts.size(), 1);

        MerchantAccount merchantAccount = merchantAccounts.get(0);
        assertTrue(merchantAccount.getCurrencyIsoCode().equals("GBP"));
        assertEquals(MerchantAccount.Status.ACTIVE, merchantAccount.getStatus());
        assertTrue(merchantAccount.isDefault());
    }

}
