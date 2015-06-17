package com.braintreegateway;

import com.braintreegateway.exceptions.ConfigurationException;

public class CredentialsParser{

    public Environment environment;
    public String clientId;
    public String clientSecret;

    public CredentialsParser(String clientId, String clientSecret) {
        if (clientSecret.startsWith("client_secret")) {
            this.clientSecret = clientSecret;
        } else {
            throw new ConfigurationException("Value passed for clientSecret is not a valid clientSecret");
        }

        if (clientId.startsWith("client_id")) {
            this.clientId = clientId;
        } else {
            throw new ConfigurationException("Value passed for clientId is not a valid clientId");
        }

        Environment clientIdEnvironment = parseEnvironment(clientId);
        Environment clientSecretEnvironment = parseEnvironment(clientSecret);

        if (clientIdEnvironment != clientSecretEnvironment) {
            throw new ConfigurationException("Mismatched credential environments: clientId environment is: " + clientIdEnvironment.getEnvironmentName() + " and clientSecret environment is: " + clientSecretEnvironment.getEnvironmentName());
        } else {
            this.environment = clientIdEnvironment;
        }
    }

    private Environment parseEnvironment(String credential) {
        String environment = credential.split("\\$")[1];
        if (environment.equals("development")) {
            return Environment.DEVELOPMENT;
        } else if (environment.equals("sandbox")) {
            return Environment.SANDBOX;
        } else if (environment.equals("production")) {
            return Environment.PRODUCTION;
        } else {
            throw new IllegalArgumentException("Unknown environment: " + environment);
        }
    }
}
