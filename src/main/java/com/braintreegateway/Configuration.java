package com.braintreegateway;

import com.braintreegateway.util.ClientLibraryProperties;

public class Configuration {
    private Environment environment;
    private String merchantId;
    private String privateKey;
    private String publicKey;
    private String clientId;
    private String clientSecret;

    public static final String VERSION = new ClientLibraryProperties().version();

    public static String apiVersion() {
        return "4";
    }

    public Configuration(Environment environment, String merchantId, String publicKey, String privateKey) {
        this.environment = environment;
        this.merchantId = merchantId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Configuration(String clientId, String clientSecret) {
        CredentialsParser parser = new CredentialsParser(clientId, clientSecret);
        this.environment = parser.environment;
        this.clientId = parser.clientId;
        this.clientSecret = parser.clientSecret;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Boolean isClientCredentials() {
        return clientId != null;
    }

    public String getMerchantPath() {
        return "/merchants/" + merchantId;
    }

    public String getBaseURL() {
        return environment.baseURL;
    }
}
