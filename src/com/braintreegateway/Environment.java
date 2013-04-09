package com.braintreegateway;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public enum Environment {
    /** For Braintree internal development. */
    DEVELOPMENT(developmentHost() + ":" + developmentPort(), developmentCertificates()),

    /** For production. */
    PRODUCTION("https://www.braintreegateway.com:443", new String[] {"www_braintreegateway_com.ca.der", "securetrust.ca.der"}),

    /** For merchant's to use during their development and testing. */
    SANDBOX("https://sandbox.braintreegateway.com:443", new String[] {"sandbox-godaddy-root.ca.der", "sandbox_braintreegateway_com.ca.der", "sandbox-godaddy-intermediate.ca.der"});

    public final String baseURL;
    public final String[] certificateFilenames;

    private Environment(String baseURL, String[] certificateFilenames) {
        this.baseURL = baseURL;
        this.certificateFilenames = certificateFilenames;
    }

    private static String developmentPort() {
        return System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
    }

    private static String developmentHost() {
        return System.getenv().get("GATEWAY_HOST") == null ? "http://localhost" : System.getenv().get("GATEWAY_HOST");
    }

    private static String[] developmentCertificates() {
        if (System.getenv().get("GATEWAY_CERTIFICATES") == null) {
            return new String[] {};
        } else {
            String certs = System.getenv().get("GATEWAY_CERTIFICATES");
            return certs.split(",");
        }
    }
}
