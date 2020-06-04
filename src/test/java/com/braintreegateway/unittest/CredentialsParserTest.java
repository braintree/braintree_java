package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CredentialsParserTest {

    @Test
    public void credentialsParserParsesClientCredentials() {
        CredentialsParser parser = new CredentialsParser("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");

        assertEquals("client_id$development$integration_client_id", parser.clientId);
        assertEquals("client_secret$development$integration_client_secret", parser.clientSecret);
        assertEquals(Environment.DEVELOPMENT, parser.environment);
    }

    @Test(expected=ConfigurationException.class)
    public void credentialsParserThrowsErrorOnInconsistentEnvironments() {
        new CredentialsParser("client_id$development$integration_client_id", "client_secret$sandbox$integration_client_secret");
    }

    @Test(expected=ConfigurationException.class)
    public void credentialsParserThrowsErrorOnInvalidClientSecret() {
        new CredentialsParser("client_id$development$integration_client_id", "client_id$development$integration_client_id");
    }

    @Test(expected=ConfigurationException.class)
    public void credentialsParserThrowsErrorOnInvalidClientId() {
        new CredentialsParser("client_secret$development$integration_client_secret", "client_secret$development$integration_client_secret");
    }

    @Test
    public void credentialsParserParsesAccessToken() {
        CredentialsParser parser = new CredentialsParser("access_token$development$integration_merchant_id$4bff9793ed");

        assertEquals("access_token$development$integration_merchant_id$4bff9793ed", parser.accessToken);
        assertEquals("integration_merchant_id", parser.merchantId);
        assertEquals(Environment.DEVELOPMENT, parser.environment);
    }

    @Test(expected=ConfigurationException.class)
    public void credentialsParserThrowsErrorOnInvalidAccessToken() {
        new CredentialsParser("client_id$development$integration_client_id");
    }
}
