package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class OAuthCredentials {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String scope;
    private Calendar expiresAt;

    public OAuthCredentials(NodeWrapper node) {
        accessToken = node.findString("access-token");
        refreshToken = node.findString("refresh-token");
        tokenType = node.findString("token-type");
        scope = node.findString("scope");
        expiresAt = node.findDateTime("expires-at");
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

    public String getScope() {
        return scope;
    }

    public Calendar getExpiresAt() {
        return expiresAt;
    }
}
