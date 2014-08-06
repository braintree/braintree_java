package com.braintreegateway;

import java.util.Arrays;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public class Environment {
    /** For Braintree internal development. */
    public static final Environment DEVELOPMENT = new Environment(developmentBaseURL() + ":" + developmentPort(), "http://auth.venmo.dev:9292", new String[] {});

    /** For production. */
    public static final Environment PRODUCTION = new Environment("https://api.braintreegateway.com:443", "https://auth.venmo.com", new String[] {"ssl/api_braintreegateway_com.ca.crt"});

    /** For merchant's to use during their development and testing. */
    public static final Environment SANDBOX = new Environment("https://api.sandbox.braintreegateway.com:443", "https://auth.sandbox.venmo.com", new String[] {"ssl/api_braintreegateway_com.ca.crt"});

    public final String baseURL;
    public final String authURL;
    public final String[] certificateFilenames;

    public Environment(String baseURL, String authURL, String[] certificateFilenames) {
        this.baseURL = baseURL;
        this.authURL = authURL;
        this.certificateFilenames = Arrays.copyOf(certificateFilenames, certificateFilenames.length);
    }

    private static String developmentBaseURL() {
        if (System.getenv().get("GATEWAY_BASE_URL") != null) {
            return System.getenv().get("GATEWAY_BASE_URL");
        } else {
            return "http://localhost";
        }
    }

    public static String developmentPort() {
        if (System.getenv().get("GATEWAY_PORT") != null) {
            return System.getenv().get("GATEWAY_PORT");
        } else {
            return "3000";
        }
    }
}
