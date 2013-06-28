package com.braintreegateway.util;

import com.braintreegateway.exceptions.UnexpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GatewayProperties {

    public static final String BRAINTREE_PROPERTY_FILE = "braintree.properties";
    public static final String VERSION_PROPERTY_NAME = "braintree.gateway.version";

    private final Properties properties = loadProperties();

    public String version() {
        return properties.getProperty(VERSION_PROPERTY_NAME);
    }

    private Properties loadProperties() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(BRAINTREE_PROPERTY_FILE);
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (IOException e) {
            throw new UnexpectedException("Couldn't load " + BRAINTREE_PROPERTY_FILE + " can't continue", e);
        }
    }
}
