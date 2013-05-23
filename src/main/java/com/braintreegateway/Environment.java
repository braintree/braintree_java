package com.braintreegateway;

import java.util.Arrays;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public enum Environment {
    /** For Braintree internal development. */
    DEVELOPMENT(developmentHost() + ":" + developmentPort(), developmentCertificates()),

    /** For production. */
    PRODUCTION("https://www.braintreegateway.com:443", new String[] {"ssl/www_braintreegateway_com.ca.der", "ssl/securetrust.ca.der"}),

    /** For merchant's to use during their development and testing. */
    SANDBOX("https://sandbox.braintreegateway.com:443", new String[] {"ssl/sandbox-godaddy-root.ca.der", "ssl/sandbox_braintreegateway_com.ca.der", "ssl/sandbox-godaddy-intermediate.ca.der"});

    public final String baseURL;
    public final String[] certificateFilenames;

    private Environment(String baseURL, String[] certificateFilenames) {
        this.baseURL = baseURL;
        this.certificateFilenames = Arrays.copyOf(certificateFilenames, certificateFilenames.length);
    }

    private static String developmentPort() {
        if (System.getenv().get("GATEWAY_PORT") != null) {
            return System.getenv().get("GATEWAY_PORT");
        } else if (System.getProperty("com.braintreegateway.port") != null) {
            return System.getProperty("com.braintreegateway.port");
        } else {
            return "3000";
        }
    }

    private static String developmentHost() {
        return System.getProperty("com.braintreegateway.host") == null ? "http://localhost" : System.getProperty("com.braintreegateway.host");
    }

    private static String[] developmentCertificates() {
        if (System.getProperty("com.braintreegateway.certificates") == null) {
            return new String[] {};
        } else {
            String certs = System.getProperty("com.braintreegateway.certificates");
            return certs.split(",");
        }
    }
}
