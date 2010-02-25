package com.braintreegateway;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public enum Environment {
    /** For Braintree internal development. */
    DEVELOPMENT("http://localhost:" + developmentPort()),
    
    /** For production. */
    PRODUCTION("https://braintreegateway.com:443"),
    
    /** For merchant's to use during their development and testing. */
    SANDBOX("https://sandbox.braintreegateway.com:443");

    public final String baseURL;

    private Environment(String baseURL) {
        this.baseURL = baseURL;
    }

    private static String developmentPort() {
        return System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
    }
}
