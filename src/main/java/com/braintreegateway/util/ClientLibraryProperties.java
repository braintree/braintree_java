package com.braintreegateway.util;

import com.braintreegateway.exceptions.UnexpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientLibraryProperties {

    public static final String BRAINTREE_PROPERTY_FILE = "braintree.properties";
    public static final String VERSION_PROPERTY_NAME = "braintree.gateway.version";

    private final Properties properties = loadProperties(BRAINTREE_PROPERTY_FILE);

    public String version() {
        return properties.getProperty(VERSION_PROPERTY_NAME);
    }

    private Properties loadProperties(String propertyFile) {
        try {
            InputStream is = null;
            try {
                is = getClass().getClassLoader().getResourceAsStream(propertyFile);
                if(is == null) {
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile);
                }
                if(is == null) {
                    throw new UnexpectedException("Couldn't load " + BRAINTREE_PROPERTY_FILE + " can't continue");
                }

                Properties p = new Properties();
                p.load(is);
                return p;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new UnexpectedException("Couldn't load " + BRAINTREE_PROPERTY_FILE + " can't continue", e);
        }
    }
}
