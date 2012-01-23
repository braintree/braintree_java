package com.braintreegateway;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public enum Environment {
    /** For Braintree internal development. */
    DEVELOPMENT("http://localhost:" + developmentPort(), new String[] {}),

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
}
