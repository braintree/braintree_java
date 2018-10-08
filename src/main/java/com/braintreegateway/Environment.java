package com.braintreegateway;

import java.util.Arrays;

/**
 * Indicates the environment of the Braintree Gateway with which to interact.
 */
public class Environment {
    /** For Braintree internal development. */
    public static final Environment DEVELOPMENT = new Environment(developmentBaseURL() + ":" + developmentPort(), "http://auth.venmo.dev:9292", new String[]{}, "development", developmentGraphQLURL());
    public static final Environment QA = new Environment("https://gateway.qa.braintreepayments.com:443", "https://auth.qa.venmo.com", new String[]{"ssl/api_braintreegateway_com.ca.crt", "ssl/payments_braintreeapi_com.ca.crt"}, "qa", "https://payments-qa.dev.braintree-api.com/graphql");

    /** For production. */
    public static final Environment PRODUCTION = new Environment("https://api.braintreegateway.com:443", "https://auth.venmo.com", new String[]{"ssl/api_braintreegateway_com.ca.crt", "ssl/payments_braintreeapi_com.ca.crt"}, "production", "https://payments.braintree-api.com/graphql");

    /** For merchants to use during their development and testing. */
    public static final Environment SANDBOX = new Environment("https://api.sandbox.braintreegateway.com:443", "https://auth.sandbox.venmo.com", new String[]{"ssl/api_braintreegateway_com.ca.crt", "ssl/payments_braintreeapi_com.ca.crt"}, "sandbox", "https://payments.sandbox.braintree-api.com/graphql");

    private String environmentName;

    public final String baseURL;
    public final String graphQLURL;
    public final String authURL;
    public final String[] certificateFilenames;

    public Environment(String baseURL, String authURL, String[] certificateFilenames, String environmentName) {
        this(baseURL, authURL, certificateFilenames, environmentName, null);
    }

    public Environment(String baseURL, String authURL, String[] certificateFilenames, String environmentName, String graphQLURL) {
        this.baseURL = baseURL;
        this.authURL = authURL;
        this.certificateFilenames = Arrays.copyOf(certificateFilenames, certificateFilenames.length);
        this.environmentName = environmentName;
        this.graphQLURL = graphQLURL;
    }

    public static Environment parseEnvironment(String environment) {
        if (environment.equals("development") || environment.equals("integration")) {
            return DEVELOPMENT;
        } else if (environment.equals("qa")) {
            return QA;
        } else if (environment.equals("sandbox")) {
            return SANDBOX;
        } else if (environment.equals("production")) {
            return PRODUCTION;
        } else {
            throw new IllegalArgumentException("Unknown environment: " + environment);
        }
    }

    private static String developmentGraphQLURL() {
        if (System.getenv().get("GRAPHQL_URL") != null) {
            return System.getenv().get("GRAPHQL_URL");
        } else {
            return "http://graphql.bt.local:8080/graphql";
        }
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

    public String getEnvironmentName() {
        return this.environmentName;
    }

    public String toString() {
        return getEnvironmentName();
    }
}
