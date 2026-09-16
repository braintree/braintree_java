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
import com.braintreegateway.Environment;
import com.braintreegateway.PaymentMethodNonce;
import com.braintreegateway.PaymentMethodNonceGateway;
import com.braintreegateway.PaymentMethodNonceRequest;
import com.braintreegateway.Request;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.SimpleNodeWrapper;

public class PaymentMethodNonceGatewayTest {
    private Http http;
    private Configuration configuration;
    private PaymentMethodNonceGateway gateway;
    private String merchantPath;
    private SimpleNodeWrapper nonceNode;

    @BeforeEach
    public void setup() {
        http = mock(Http.class);
        configuration = new Configuration(
                Environment.DEVELOPMENT, "integration_merchant_id", "public_key", "private_key");
        gateway = new PaymentMethodNonceGateway(http, configuration);
        merchantPath = configuration.getMerchantPath();
        nonceNode = SimpleNodeWrapper.parse("<payment-method-nonce><nonce>a-nonce</nonce></payment-method-nonce>");
        when(http.get(anyString())).thenReturn(nonceNode);
        when(http.post(anyString())).thenReturn(nonceNode);
        when(http.post(anyString(), nullable(Request.class))).thenReturn(nonceNode);
    }

    @Test
    public void createByTokenPostsToCorrectUrl() {
        Result<PaymentMethodNonce> result = gateway.create("a-token");

        assertTrue(result.isSuccess());
        verify(http).post(merchantPath + "/payment_methods/a-token/nonces");
    }

    @Test
    public void createByRequestPostsToCorrectUrl() {
        PaymentMethodNonceRequest request = new PaymentMethodNonceRequest()
                .paymentMethodToken("a-token")
                .authenticationInsight(true);

        Result<PaymentMethodNonce> result = gateway.create(request);

        assertTrue(result.isSuccess());
        verify(http).post(eq(merchantPath + "/payment_methods/a-token/nonces"), nullable(Request.class));
    }

    @Test
    public void findReturnsPaymentMethodNonce() {
        when(http.get(merchantPath + "/payment_method_nonces/a-nonce")).thenReturn(nonceNode);

        PaymentMethodNonce nonce = gateway.find("a-nonce");

        assertEquals("a-nonce", nonce.getNonce());
        verify(http).get(merchantPath + "/payment_method_nonces/a-nonce");
    }

    @Test
    public void createThrowsNotFoundForTraversalToken() {
        assertThrows(NotFoundException.class, () -> gateway.create("../../foo"));
    }

    @Test
    public void createWithRequestThrowsNotFoundForTraversalToken() {
        PaymentMethodNonceRequest request = new PaymentMethodNonceRequest();
        request.paymentMethodToken("../../foo");
        assertThrows(NotFoundException.class, () -> gateway.create(request));
    }

    @Test
    public void findThrowsNotFoundForTraversalNonce() {
        assertThrows(NotFoundException.class, () -> gateway.find("../../foo"));
    }

}
