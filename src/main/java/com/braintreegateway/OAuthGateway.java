package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class OAuthGateway {

    private Http http;

    public OAuthGateway(Http http) {
        this.http = http;
    }

    public Result<OAuthCredentials> createTokenFromCode(OAuthCredentialsRequest request) {
        request.grantType("authorization_code");

        NodeWrapper response = http.post("/oauth/access_tokens", request);
        return new Result<OAuthCredentials>(response, OAuthCredentials.class);
    }

    public Result<OAuthCredentials> createTokenFromRefreshToken(OAuthCredentialsRequest request) {
        request.grantType("refresh_token");

        NodeWrapper response = http.post("/oauth/access_tokens", request);
        return new Result<OAuthCredentials>(response, OAuthCredentials.class);
    }
}
