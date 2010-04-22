package com.braintreegateway;

public class Configuration {
    public String baseMerchantURL;
    public String privateKey;
    public String publicKey;
    
    public static String apiVersion() {
        return "2";
    }

    public Configuration(String baseMerchantURL, String publicKey, String privateKey) {
        this.baseMerchantURL = baseMerchantURL;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
