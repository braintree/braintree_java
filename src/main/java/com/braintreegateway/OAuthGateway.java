package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.Sha256Hasher;

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

    public String connectUrl(OAuthConnectUrlRequest request) {
        request.clientId(configuration.getClientId());
        String queryString = request.toQueryString();
        String url = configuration.getBaseURL() + "/oauth/connect?" + queryString;
        return String.format("%1$s&signature=%2$s&algorithm=SHA256", url, computeSignature(url));
    }

    private String computeSignature(String url) {
        Sha256Hasher hasher = new Sha256Hasher();
        return hasher.hmacHash(configuration.getClientSecret(), url);
    }
}
