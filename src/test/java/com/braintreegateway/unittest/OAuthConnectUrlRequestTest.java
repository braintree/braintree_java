package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.OAuthConnectUrlRequest;

public class OAuthConnectUrlRequestTest {

    @Test
    public void buildsQueryStringWithTopLevelFieldsPaymentMethodsAndNestedRequests() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest()
                .merchantId("a-merchant-id")
                .scope("read_write")
                .clientId("a-client-id")
                .state("a-state")
                .redirectUri("https://example.com/callback")
                .landingPage("login")
                .paymentMethods(new String[] {"credit_card", "paypal"});

        OAuthConnectUrlRequest fromUser = request.user().firstName("Dan").done();
        OAuthConnectUrlRequest fromBusiness = request.business().name("Braintree").done();
        assertSame(request, fromUser);
        assertSame(request, fromBusiness);

        String queryString = request.toQueryString();
        // Assert full key/value pairs rather than bare substrings, to avoid false positives.
        assertTrue(queryString.contains("merchant_id=a-merchant-id"), queryString);
        assertTrue(queryString.contains("scope=read_write"), queryString);
        assertTrue(queryString.contains("client_id=a-client-id"), queryString);
        assertTrue(queryString.contains("redirect_uri=https%3A%2F%2Fexample.com%2Fcallback"), queryString);
        assertTrue(queryString.contains("payment_methods%5B%5D=credit_card"), queryString);
        assertTrue(queryString.contains("payment_methods%5B%5D=paypal"), queryString);
        // nested user[...] and business[...] params (brackets URL-encoded as %5B / %5D)
        assertTrue(queryString.contains("user%5Bfirst_name%5D=Dan"), queryString);
        assertTrue(queryString.contains("business%5Bname%5D=Braintree"), queryString);
        // signupOnly defaults to null -> not included
        assertFalse(queryString.contains("signup_only"), queryString);
    }

    @Test
    public void includesSignupOnlyWhenSetAndLoginOnlyUnset() {
        String queryString = new OAuthConnectUrlRequest().signupOnly(true).toQueryString();

        assertTrue(queryString.contains("signup_only"), queryString);
    }

    @Test
    public void includesSignupOnlyWhenSignupOnlyTrueAndLoginOnlyFalse() {
        String queryString = new OAuthConnectUrlRequest().signupOnly(true).loginOnly(false).toQueryString();

        assertTrue(queryString.contains("signup_only"), queryString);
    }

    @Test
    public void omitsSignupOnlyWhenSignupOnlyTrueAndLoginOnlyTrue() {
        String queryString = new OAuthConnectUrlRequest().signupOnly(true).loginOnly(true).toQueryString();

        assertFalse(queryString.contains("signup_only"), queryString);
    }

    @Test
    public void omitsSignupOnlyWhenSignupOnlyFalseAndLoginOnlySet() {
        String queryString = new OAuthConnectUrlRequest().signupOnly(false).loginOnly(true).toQueryString();

        assertFalse(queryString.contains("signup_only"), queryString);
    }
}
