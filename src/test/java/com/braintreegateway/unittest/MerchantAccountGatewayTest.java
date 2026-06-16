package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.MerchantAccountCreateForCurrencyRequest;
import com.braintreegateway.MerchantAccountGateway;
import com.braintreegateway.Request;
import com.braintreegateway.PaginatedResult;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class MerchantAccountGatewayTest {
    private Http http;
    private Configuration configuration;
    private MerchantAccountGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new MerchantAccountGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();
    }

    @Test
    public void createForCurrencyPostsToCorrectUrl() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<merchant-account><id>ma_id</id><status>active</status></merchant-account>");
        when(http.post(eq(merchantPath + MerchantAccountGateway.CREATE_FOR_CURRENCY_URL),
                nullable(Request.class))).thenReturn(node);

        Result<MerchantAccount> result = gateway.createForCurrency(
                new MerchantAccountCreateForCurrencyRequest().currency("USD"));

        assertTrue(result.isSuccess());
        verify(http).post(eq(merchantPath + MerchantAccountGateway.CREATE_FOR_CURRENCY_URL),
                nullable(Request.class));
    }

    @Test
    public void findReturnsMerchantAccount() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<merchant-account><id>ma_id</id><status>active</status></merchant-account>");
        when(http.get(merchantPath + "/merchant_accounts/ma_id")).thenReturn(node);

        MerchantAccount result = gateway.find("ma_id");

        assertEquals("ma_id", result.getId());
        verify(http).get(merchantPath + "/merchant_accounts/ma_id");
    }

    @Test
    public void findThrowsNotFoundWhenIdIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.find(null));
    }

    @Test
    public void findThrowsNotFoundWhenIdIsBlank() {
        assertThrows(NotFoundException.class, () -> gateway.find("   "));
    }

    @Test
    public void fetchMerchantAccountsReturnsPagedResults() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<merchant-accounts>" +
                "<total-items type=\"integer\">1</total-items>" +
                "<page-size type=\"integer\">50</page-size>" +
                "<merchant-account><id>ma_id</id><status>active</status></merchant-account>" +
                "</merchant-accounts>");
        when(http.get(merchantPath + "/merchant_accounts?page=1")).thenReturn(node);

        PaginatedResult<MerchantAccount> result = gateway.fetchMerchantAccounts(1);

        assertEquals(1, result.getTotalItems());
        assertEquals(1, result.getCurrentPage().size());
        assertEquals("ma_id", result.getCurrentPage().get(0).getId());
        verify(http).get(merchantPath + "/merchant_accounts?page=1");
    }

}
