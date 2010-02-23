package com.braintreegateway;

public enum Environment {
    DEVELOPMENT("http://localhost:" + developmentPort()),
    SANDBOX("https://sandbox.braintreegateway.com:443"),
    PRODUCTION("https://braintreegateway.com:443");

    public final String baseURL;

    private Environment(String baseURL) {
        this.baseURL = baseURL;
    }

    private static String developmentPort() {
        return System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
    }
}
