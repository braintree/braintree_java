package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.Request;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.UsBankAccountVerification;
import com.braintreegateway.UsBankAccountVerificationConfirmRequest;
import com.braintreegateway.UsBankAccountVerificationGateway;
import com.braintreegateway.UsBankAccountVerificationSearchRequest;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class UsBankAccountVerificationGatewayTest {
    private Http http;
    private Configuration configuration;
    private UsBankAccountVerificationGateway gateway;
    private String merchantPath;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new UsBankAccountVerificationGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();
    }

    @Test
    public void findThrowsNotFoundWhenIdIsNull() {
        assertThrows(NotFoundException.class, () -> gateway.find(null));
    }

    @Test
    public void findThrowsNotFoundWhenIdIsEmpty() {
        assertThrows(NotFoundException.class, () -> gateway.find(""));
    }

    @Test
    public void findThrowsNotFoundWhenIdIsBlank() {
        assertThrows(NotFoundException.class, () -> gateway.find("   "));
    }

    @Test
    public void findReturnsVerification() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<us-bank-account-verification>" +
                "<id>verification_id</id>" +
                "<status>verified</status>" +
                "</us-bank-account-verification>");
        when(http.get(merchantPath + "/us_bank_account_verifications/verification_id")).thenReturn(node);

        UsBankAccountVerification verification = gateway.find("verification_id");

        assertEquals("verification_id", verification.getId());
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
        verify(http).get(merchantPath + "/us_bank_account_verifications/verification_id");
    }

    @Test
    public void searchReturnsResourceCollectionAndPagesThroughResults() {
        SimpleNodeWrapper searchIdsNode = SimpleNodeWrapper.parse(
                "<search-results>" +
                "<page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\">" +
                "<item>id_one</item>" +
                "<item>id_two</item>" +
                "</ids>" +
                "</search-results>");
        when(http.post(eq(merchantPath + "/us_bank_account_verifications/advanced_search_ids"), any(Request.class)))
                .thenReturn(searchIdsNode);

        // The pager (getFirst/iteration) calls back into fetchUsBankAccountVerifications.
        SimpleNodeWrapper pageNode = SimpleNodeWrapper.parse(
                "<us-bank-account-verifications type=\"array\">" +
                "<us-bank-account-verification><id>id_one</id></us-bank-account-verification>" +
                "</us-bank-account-verifications>");
        when(http.post(eq(merchantPath + "/us_bank_account_verifications/advanced_search"), any(Request.class)))
                .thenReturn(pageNode);

        UsBankAccountVerificationSearchRequest query = new UsBankAccountVerificationSearchRequest();
        ResourceCollection<UsBankAccountVerification> collection = gateway.search(query);

        assertEquals(2, collection.getMaximumSize());
        assertEquals(Arrays.asList("id_one", "id_two"), collection.getIds());
        assertEquals("id_one", collection.getFirst().getId());
    }

    @Test
    public void confirmMicroTransferAmountsReturnsResult() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<us-bank-account-verification>" +
                "<id>verification_id</id>" +
                "<status>verified</status>" +
                "</us-bank-account-verification>");
        when(http.put(
                eq(merchantPath + "/us_bank_account_verifications/verification_id/confirm_micro_transfer_amounts"),
                any(Request.class))).thenReturn(node);

        UsBankAccountVerificationConfirmRequest request =
                new UsBankAccountVerificationConfirmRequest().depositAmounts(Arrays.asList(17, 29));
        Result<UsBankAccountVerification> result =
                gateway.confirmMicroTransferAmounts("verification_id", request);

        assertTrue(result.isSuccess());
        assertEquals("verification_id", result.getTarget().getId());
    }
}
