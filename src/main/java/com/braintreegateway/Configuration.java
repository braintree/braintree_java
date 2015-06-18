package com.braintreegateway;

import com.braintreegateway.util.ClientLibraryProperties;

public class Configuration {
    public String baseMerchantURL;
    public String privateKey;
    public String publicKey;
    public Environment environment;

    public static final String VERSION = new ClientLibraryProperties().version();

    public static String apiVersion() {
        return "4";
    }

    public Configuration(String baseMerchantURL, String publicKey, String privateKey, Environment environment) {
        this.baseMerchantURL = baseMerchantURL;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.environment = environment;
    }
}
