package com.braintreegateway;

public class OAuthRevokeAccessTokenRequest extends Request {

    public String token;

    public OAuthRevokeAccessTokenRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        return new RequestBuilder("credentials").
            addElement("token", token).
            toXML();
    }
}
