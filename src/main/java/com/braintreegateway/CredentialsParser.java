package com.braintreegateway;

import com.braintreegateway.exceptions.ConfigurationException;

public class CredentialsParser {

    public Environment environment;
    public String merchantId;
    public String clientId;
    public String clientSecret;
    public String accessToken;

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

    public CredentialsParser(String accessToken) {
        if (accessToken.startsWith("access_token")) {
            this.accessToken = accessToken;
        } else {
            throw new ConfigurationException("Value passed for accessToken is not a valid accessToken");
        }

        this.merchantId = parseMerchantId(accessToken);
        this.environment = parseEnvironment(accessToken);
    }

    private Environment parseEnvironment(String credential) {
        String environment = credential.split("\\$")[1];
        return Environment.parseEnvironment(environment);
    }

    private String parseMerchantId(String accessToken) {
        return accessToken.split("\\$")[2];
    }
}
