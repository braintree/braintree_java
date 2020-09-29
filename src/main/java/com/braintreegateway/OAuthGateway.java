package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

public class OAuthGateway {

    private Http http;
    private Configuration configuration;

    public OAuthGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
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

    public Result<OAuthResult> revokeAccessToken(String accessToken) {
        OAuthRevokeAccessTokenRequest request = new OAuthRevokeAccessTokenRequest()
            .token(accessToken);

        NodeWrapper response = http.post("/oauth/revoke_access_token", request);
        return new Result<OAuthResult>(response, OAuthResult.class);
    }

    public String connectUrl(OAuthConnectUrlRequest request) {
        request.clientId(configuration.getClientId());
        String queryString = request.toQueryString();
        return configuration.getBaseURL() + "/oauth/connect?" + queryString;
    }
}
