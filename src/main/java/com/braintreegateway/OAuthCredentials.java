package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class OAuthCredentials {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String expiresAt;

    public OAuthCredentials(NodeWrapper node) {
        accessToken = node.findString("access-token");
        refreshToken = node.findString("refresh-token");
        tokenType = node.findString("token-type");
        expiresAt = node.findString("expires-at");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}
