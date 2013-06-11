package com.braintreegateway;

import java.util.Arrays;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public class Environment {
    /** For Braintree internal development. */
    public static final Environment DEVELOPMENT = new Environment("http://localhost:3000", new String[] {});

    /** For production. */
    public static final Environment PRODUCTION = new Environment("https://www.braintreegateway.com:443", new String[] {"ssl/www_braintreegateway_com.ca.der", "ssl/securetrust.ca.der"});

    /** For merchant's to use during their development and testing. */
    public static final Environment SANDBOX = new Environment("https://sandbox.braintreegateway.com:443", new String[] {"ssl/sandbox-godaddy-root.ca.der", "ssl/sandbox_braintreegateway_com.ca.der", "ssl/sandbox-godaddy-intermediate.ca.der"});

    public final String baseURL;
    public final String[] certificateFilenames;

    public Environment(String baseURL, String[] certificateFilenames) {
        this.baseURL = baseURL;
        this.certificateFilenames = Arrays.copyOf(certificateFilenames, certificateFilenames.length);
    }
}
