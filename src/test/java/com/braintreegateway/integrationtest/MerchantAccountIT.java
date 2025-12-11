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
        TestHelper.MerchantResult merchantResult = TestHelper.getMerchant();
        this.gateway = new BraintreeGateway(merchantResult.getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("AUD");

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertTrue(result.isSuccess());
        assertEquals("AUD", result.getTarget().getCurrencyIsoCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesMerchantAccountExistsForCurrency() {
        TestHelper.MerchantResult merchantResult = TestHelper.getMerchant();
        this.gateway = new BraintreeGateway(merchantResult.getCredentials().getAccessToken());

        // Create a merchant account for CAD
        MerchantAccountCreateForCurrencyRequest createRequest = new MerchantAccountCreateForCurrencyRequest().
            currency("CAD");
        Result<MerchantAccount> createResult = gateway.merchantAccount().createForCurrency(createRequest);
        assertTrue(createResult.isSuccess());

        // Try to create another merchant account for the same currency
        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("CAD");

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("currency");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_MERCHANT_ACCOUNT_EXISTS_FOR_CURRENCY, errors.get(0).getCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesCurrencyIsInvalid() {
        TestHelper.MerchantResult merchantResult = TestHelper.getMerchant();
        this.gateway = new BraintreeGateway(merchantResult.getCredentials().getAccessToken());

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest();

        Result<MerchantAccount> result = gateway.merchantAccount().createForCurrency(request);
        assertFalse(result.isSuccess());

        List<ValidationError> errors = result.getErrors().forObject("merchant").onField("currency");
        assertEquals(1, errors.size());
        assertEquals(ValidationErrorCode.MERCHANT_CURRENCY_IS_REQUIRED, errors.get(0).getCode());
    }

    @Test
    public void createMerchantAccountForCurrencyHandlesCurrencyIsRequired() {
        TestHelper.MerchantResult merchantResult = TestHelper.getMerchant();
        this.gateway = new BraintreeGateway(merchantResult.getCredentials().getAccessToken());

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
        TestHelper.MerchantResult merchantResult = TestHelper.getMerchant();
        this.gateway = new BraintreeGateway(merchantResult.getCredentials().getAccessToken());

        // Get the existing merchant accounts to use one's ID
        String existingMerchantAccountId = merchantResult.getMerchantAccounts().get(0).getId();

        MerchantAccountCreateForCurrencyRequest request = new MerchantAccountCreateForCurrencyRequest().
            currency("GBP").
            id(existingMerchantAccountId);

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

        String code = TestHelper.createOAuthGrant(gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
            code(code).
            scope("read_write");

        Result<OAuthCredentials> accessTokenResult = gateway.oauth().createTokenFromCode(oauthRequest);

        this.gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        PaginatedCollection<MerchantAccount> result = gateway.merchantAccount().all();
        List<MerchantAccount> merchantAccounts = new ArrayList<MerchantAccount>();
        for (MerchantAccount merchantAccount : result) {
            merchantAccounts.add(merchantAccount);
        }

        assertTrue(merchantAccounts.size() > 0);

        MerchantAccount merchantAccount = merchantAccounts.get(0);
        assertEquals(MerchantAccount.Status.ACTIVE, merchantAccount.getStatus());
    }

}
