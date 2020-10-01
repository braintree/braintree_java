package com.braintreegateway;

public class OAuthCredentialsRequest extends Request {

    public String code;
    public String scope;
    public String grantType;
    public String refreshToken;

    public OAuthCredentialsRequest code(String code) {
        this.code = code;
        return this;
    }

    public OAuthCredentialsRequest scope(String scope) {
        this.scope = scope;
        return this;
    }

    public OAuthCredentialsRequest grantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public OAuthCredentialsRequest refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Override
    public String toXML() {
        return new RequestBuilder("credentials")
            .addElement("code", code)
            .addElement("refreshToken", refreshToken)
            .addElement("scope", scope)
            .addElement("grantType", grantType)
            .toXML();
    }
}
