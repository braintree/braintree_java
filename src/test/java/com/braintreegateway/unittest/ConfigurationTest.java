package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void testStringEnvironmentConstructor() {
       Configuration configuration = new Configuration("development", "merchant_id", "public_key", "private_key");
       assertEquals(Environment.DEVELOPMENT, configuration.getEnvironment());
    }

    @Test
    public void configurationUsesNoProxyIfNotSpecified() {
        Configuration configuration = new Configuration(Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

        assertNull(configuration.getProxy());
    }

    @Test
    public void configurationReturnsProxyObject() {
        Configuration configuration = new Configuration(Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

        configuration.setProxy("localhost", 3000);

        assertNotNull(configuration.getProxy());
    }

    @Test
    public void testConfigurationIsUsingProxy() {
        Configuration configuration = new Configuration(Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

        assertFalse(configuration.usesProxy());

        configuration.setProxy("localhost", 3000);

        assertTrue(configuration.usesProxy());
    }

    @Test
    public void testReadtimeoutDefaultsToSixtySeconds() {
        Configuration configuration = new Configuration(Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");
        assertEquals(configuration.getTimeout(), 60000);
    }

    @Test
    public void testSettingReadTimeout() {
        Configuration configuration = new Configuration(Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");
        configuration.setTimeout(30000);
        assertEquals(configuration.getTimeout(), 30000);
    }
}
