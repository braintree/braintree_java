package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.CreditCardVerification;
import com.braintreegateway.CreditCardVerificationGateway;
import com.braintreegateway.CreditCardVerificationRequest;
import com.braintreegateway.CreditCardVerificationSearchRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Request;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class CreditCardVerificationGatewayTest {
    private Http http;
    private Configuration configuration;
    private CreditCardVerificationGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper verificationNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new CreditCardVerificationGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();

        verificationNode = SimpleNodeWrapper.parse("<verification><id>verification_id</id></verification>");

        when(http.get(anyString())).thenReturn(verificationNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(verificationNode);
    }

    @Test
    public void findReturnsVerification() {
        when(http.get(merchantPath + "/verifications/verification_id")).thenReturn(verificationNode);

        assertEquals("verification_id", gateway.find("verification_id").getId());
        verify(http).get(merchantPath + "/verifications/verification_id");
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
    public void create() {
        Result<CreditCardVerification> result = gateway.create(new CreditCardVerificationRequest());

        assertTrue(result.isSuccess());
        assertEquals("verification_id", result.getTarget().getId());
        verify(http).post(eq(merchantPath + "/verifications"), nullable(Request.class));
    }

    @Test
    public void searchReturnsResourceCollectionAndPagesResults() {
        SimpleNodeWrapper searchResults = SimpleNodeWrapper.parse(
                "<search-results><page-size type=\"integer\">50</page-size>" +
                "<ids type=\"array\"><item>verification_id</item></ids></search-results>");
        when(http.post(eq(merchantPath + "/verifications/advanced_search_ids"), nullable(Request.class)))
                .thenReturn(searchResults);

        SimpleNodeWrapper page = SimpleNodeWrapper.parse(
                "<credit-card-verifications type=\"array\">" +
                "<verification><id>verification_id</id></verification></credit-card-verifications>");
        when(http.post(eq(merchantPath + "/verifications/advanced_search"), nullable(Request.class)))
                .thenReturn(page);

        ResourceCollection<CreditCardVerification> collection =
                gateway.search(new CreditCardVerificationSearchRequest());

        assertEquals(1, collection.getMaximumSize());
        assertEquals("verification_id", collection.getFirst().getId());
    }

    @Test
    public void findThrowsNotFoundForTraversalId() {
        assertThrows(NotFoundException.class, () -> gateway.find("../../foo"));
    }

}
