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
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
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
}
