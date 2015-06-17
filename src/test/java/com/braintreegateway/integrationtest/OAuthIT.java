package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OAuthIT {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
    }

    @Test
    public void createTokenFromCodeReturnsOAuthCredentials() {
        String code = TestHelper.createOAuthGrant(gateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthCredentials = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> result = gateway.oauth().createTokenFromCode(oauthCredentials);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget().getAccessToken());
        assertNotNull(result.getTarget().getRefreshToken());
        assertNotNull(result.getTarget().getExpiresAt());
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
}

