package com.braintreegateway.util;

public class SignatureService {
    private final String key;
    private final Hasher hasher;

    private final String shaAlgorithm;

    public SignatureService(String key, Hasher hasher, String shaAlgorithm) {
        this.key = key;
        this.hasher = hasher;
        this.shaAlgorithm = shaAlgorithm;
    }

    public String sign(String query) {
        return hash(query) + "|" + query;
    }

    private String hash(String string) {
        return hasher.hmacHash(key, string, shaAlgorithm);
    }
}
