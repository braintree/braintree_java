package com.braintreegateway.util;

public class SignatureService {
    private final String key;
    private final Hasher hasher;

    public SignatureService(String key, Hasher hasher) {
        this.key = key;
        this.hasher = hasher;
    }

    public String sign(String query) {
        return hash(query) + "|" + query;
    }

    private String hash(String string) {
        return hasher.hmacHash(key, string);
    }
}
