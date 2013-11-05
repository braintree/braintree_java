package com.braintreegateway.util;

public class SignatureService {
    private final String key;
    private final Hasher hasher;

    public SignatureService(String key, Hasher hasher) {
        this.key = key;
        this.hasher = hasher;
    }

    public String sign(QueryString query) {
        String urlEncodedData = query.toString();
        return hash(urlEncodedData) + "|" + urlEncodedData;
    }

    private String hash(String string) {
        return hasher.hmacHash(key, string);
    }

}

