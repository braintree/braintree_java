package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.ConfigurationException;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

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
}
