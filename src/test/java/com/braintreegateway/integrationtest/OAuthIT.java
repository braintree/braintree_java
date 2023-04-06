package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.testhelpers.TestHelper;

import java.util.*;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class OAuthIT extends IntegrationTest {

    @BeforeEach
    public void createGateway() {
        this.gateway = new BraintreeGateway("client_id$development$integration_client_id",
           "client_secret$development$integration_client_secret"
        );
    }

    @Test
    public void createTokenFromCodeReturnsOAuthCredentials() {
        String code = TestHelper.createOAuthGrant(gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthCredentials = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> result = gateway.oauth().createTokenFromCode(oauthCredentials);

        assertTrue(result.isSuccess());
        assertTrue(result.getTarget().getAccessToken().startsWith("access_token"));
        assertTrue(result.getTarget().getExpiresAt().after(Calendar.getInstance()));
        assertTrue(result.getTarget().getRefreshToken().startsWith("refresh_token"));
        assertEquals("bearer", result.getTarget().getTokenType());
    }

    @Test
    public void createTokenFromBadCodeReturnsOAuthCredentials() {
        OAuthCredentialsRequest oauthCredentials = new OAuthCredentialsRequest().
             code("bad_code").
             scope("read_write");

        Result<OAuthCredentials> result = gateway.oauth().createTokenFromCode(oauthCredentials);
        ValidationErrors errors = result.getErrors();

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.OAUTH_INVALID_GRANT, errors.forObject("credentials").onField("code").get(0).getCode());
        assertEquals("Invalid grant: code not found", errors.forObject("credentials").onField("code").get(0).getMessage());
    }

    @Test
    public void createTokenFromRefreshToken() {
        String code = TestHelper.createOAuthGrant(gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthCredentials = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> result = gateway.oauth().createTokenFromCode(oauthCredentials);

        OAuthCredentialsRequest refreshTokenRequest = new OAuthCredentialsRequest().
            refreshToken(result.getTarget().getRefreshToken()).
            scope("read_write");

        Result<OAuthCredentials> refreshTokenResult = gateway.oauth().createTokenFromRefreshToken(refreshTokenRequest);

        assertTrue(refreshTokenResult.isSuccess());
        assertNotNull(refreshTokenResult.getTarget().getAccessToken());
        assertNotNull(refreshTokenResult.getTarget().getRefreshToken());
        assertNotNull(refreshTokenResult.getTarget().getExpiresAt());
        assertEquals("bearer", refreshTokenResult.getTarget().getTokenType());
    }

    @Test
    public void revokeAccessToken() {
        String code = TestHelper.createOAuthGrant(gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthCredentials = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> result = gateway.oauth().createTokenFromCode(oauthCredentials);

        String accessToken = result.getTarget().getAccessToken();
        Result<OAuthResult> revokeAccessTokenResult = gateway.oauth().revokeAccessToken(accessToken);

        assertTrue(revokeAccessTokenResult.isSuccess());
        assertTrue(revokeAccessTokenResult.getTarget().getResult());

        gateway = new BraintreeGateway(accessToken);

        assertThrows(AuthenticationException.class, () -> {
            gateway.customer().create(new CustomerRequest());
        });
    }

    @Test
    public void connectUrlReturnsCorrectUrl() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest().
            merchantId("integration_merchant_id").
            redirectUri("http://bar.example.com").
            scope("read_write").
            state("baz_state").
            landingPage("login").
            loginOnly(true).
            user().
                country("USA").
                email("foo@example.com").
                firstName("Bob").
                lastName("Jones").
                phone("555-555-5555").
                dobYear("1970").
                dobMonth("01").
                dobDay("01").
                streetAddress("222 W Merchandise Mart").
                locality("Chicago").
                region("IL").
                postalCode("60606").
                done().
            business().
                name("14 Ladders").
                registeredAs("14.0 Ladders").
                industry("Ladders").
                description("We sell the best ladders").
                streetAddress("111 N Canal").
                locality("Chicago").
                region("IL").
                postalCode("60606").
                country("USA").
                annualVolumeAmount("1000000").
                averageTransactionAmount("100").
                maximumTransactionAmount("10000").
                shipPhysicalGoods(true).
                fulfillmentCompletedIn(7).
                currency("USD").
                website("http://example.com").
                establishedOn("1988-10").
                done();

        String urlString = gateway.getConfiguration().connectUrl(request);

        URL url;

        try {
            url = new URL(urlString);
            assertEquals("localhost", url.getHost());
            assertEquals("/oauth/connect", url.getPath());

            Map<String, String> query = TestHelper.splitQuery(url);

            assertEquals("integration_merchant_id", query.get("merchant_id"));
            assertEquals("client_id$development$integration_client_id", query.get("client_id"));
            assertEquals("http://bar.example.com", query.get("redirect_uri"));
            assertEquals("read_write", query.get("scope"));
            assertEquals("baz_state", query.get("state"));
            assertEquals("login", query.get("landing_page"));
            assertEquals("true", query.get("login_only"));
            assertNull(query.get("signup_only"));

            assertEquals("USA", query.get("user[country]"));

            assertEquals("USA", query.get("user[country]"));
            assertEquals("foo@example.com", query.get("user[email]"));
            assertEquals("Bob", query.get("user[first_name]"));
            assertEquals("Jones", query.get("user[last_name]"));
            assertEquals("555-555-5555", query.get("user[phone]"));
            assertEquals("1970", query.get("user[dob_year]"));
            assertEquals("01", query.get("user[dob_month]"));
            assertEquals("01", query.get("user[dob_day]"));
            assertEquals("222 W Merchandise Mart", query.get("user[street_address]"));
            assertEquals("Chicago", query.get("user[locality]"));
            assertEquals("IL", query.get("user[region]"));
            assertEquals("60606", query.get("user[postal_code]"));

            assertEquals("14 Ladders", query.get("business[name]"));
            assertEquals("14.0 Ladders", query.get("business[registered_as]"));
            assertEquals("Ladders", query.get("business[industry]"));
            assertEquals("We sell the best ladders", query.get("business[description]"));
            assertEquals("111 N Canal", query.get("business[street_address]"));
            assertEquals("Chicago", query.get("business[locality]"));
            assertEquals("IL", query.get("business[region]"));
            assertEquals("60606", query.get("business[postal_code]"));
            assertEquals("USA", query.get("business[country]"));
            assertEquals("1000000", query.get("business[annual_volume_amount]"));
            assertEquals("100", query.get("business[average_transaction_amount]"));
            assertEquals("10000", query.get("business[maximum_transaction_amount]"));
            assertEquals("true", query.get("business[ship_physical_goods]"));
            assertEquals("7", query.get("business[fulfillment_completed_in]"));
            assertEquals("USD", query.get("business[currency]"));
            assertEquals("http://example.com", query.get("business[website]"));
            assertEquals("1988-10", query.get("business[established_on]"));
        } catch (java.io.UnsupportedEncodingException e) {
            fail("unsupported encoding");
        } catch (java.net.MalformedURLException e) {
            fail("malformed url");
        }
    }

    @Test
    public void connectUrlReturnsCorrectUrlWithoutOptionalParams() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest();

        String urlString = gateway.getConfiguration().connectUrl(request);

        URL url;

        try {
            url = new URL(urlString);

            Map<String, String> query = TestHelper.splitQuery(url);

            assertNull(query.get("redirect_uri"));
        } catch (java.io.UnsupportedEncodingException e) {
            fail("unsupported encoding");
        } catch (java.net.MalformedURLException e) {
            fail("malformed url");
        }
    }

    @Test
    public void connectUrlReturnsCorrectPaymentMethods() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest().
            paymentMethods(new String[] {"credit_card", "paypal"});

        String urlString = gateway.getConfiguration().connectUrl(request);

        URL url;

        try {
            url = new URL(urlString);

            Map<String, String> query = TestHelper.splitQuery(url);

            assertNull(query.get("redirect_uri"));
            assertEquals("credit_card, paypal", query.get("payment_methods[]"));
        } catch (java.io.UnsupportedEncodingException e) {
            fail("unsupported encoding");
        } catch (java.net.MalformedURLException e) {
            fail("malformed url");
        }
    }

    @Test
    public void connectUrlCanIncludeSignupOnly() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest()
            .signupOnly(true);

        String urlString = gateway.getConfiguration().connectUrl(request);

        try {
            URL url = new URL(urlString);

            Map<String, String> query = TestHelper.splitQuery(url);

            assertEquals("true", query.get("signup_only"));
        } catch (java.io.UnsupportedEncodingException e) {
            fail("unsupported encoding");
        } catch (java.net.MalformedURLException e) {
            fail("malformed url");
        }
    }

    @Test
    public void connectUrlOnlyIncludesLoginOnlyIfBothLoginOnlyAndSignupOnlyAreSpecified() {
        OAuthConnectUrlRequest request = new OAuthConnectUrlRequest()
            .loginOnly(true)
            .signupOnly(true);

        String urlString = gateway.getConfiguration().connectUrl(request);

        try {
            URL url = new URL(urlString);

            Map<String, String> query = TestHelper.splitQuery(url);

            assertEquals("true", query.get("login_only"));
            assertNull(query.get("signup_only"));
        } catch (java.io.UnsupportedEncodingException e) {
            fail("unsupported encoding");
        } catch (java.net.MalformedURLException e) {
            fail("malformed url");
        }
    }
}
