package com.braintreegateway;

public class OAuthConnectUrlRequest extends Request {

    private String merchantId;
    private String scope;
    private String clientId;
    private String state;
    private String redirectUri;
    private String landingPage;
    private Boolean loginOnly;
    private Boolean signupOnly;
    private String[] paymentMethods = new String[0];

    private OAuthConnectUrlUserRequest user;
    private OAuthConnectUrlBusinessRequest business;

    public OAuthConnectUrlRequest merchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public OAuthConnectUrlRequest scope(String scope) {
        this.scope = scope;
        return this;
    }

    public OAuthConnectUrlRequest clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public OAuthConnectUrlRequest state(String state) {
        this.state = state;
        return this;
    }

    public OAuthConnectUrlRequest landingPage(String landingPage) {
        this.landingPage = landingPage;
        return this;
    }

    public OAuthConnectUrlRequest loginOnly(Boolean loginOnly) {
        this.loginOnly = loginOnly;
        return this;
    }

    public OAuthConnectUrlRequest signupOnly(Boolean signupOnly) {
        this.signupOnly = signupOnly;
        return this;
    }

    private Boolean isSignupOnly() {
        if (this.signupOnly == null) {
            return false;
        }
        if (this.loginOnly == null) {
            return this.signupOnly;
        }
        return this.signupOnly && !this.loginOnly;
    }

    public OAuthConnectUrlRequest redirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public OAuthConnectUrlRequest paymentMethods(String[] paymentMethods) {
        this.paymentMethods = paymentMethods;
        return this;
    }

    public OAuthConnectUrlUserRequest user() {
        user = new OAuthConnectUrlUserRequest(this);
        return this.user;
    }

    public OAuthConnectUrlBusinessRequest business() {
        business = new OAuthConnectUrlBusinessRequest(this);
        return this.business;
    }

    @Override
    public String toQueryString() {
        RequestBuilder builder = new RequestBuilder("")
            .addTopLevelElement("merchantId", merchantId)
            .addTopLevelElement("scope", scope)
            .addTopLevelElement("clientId", clientId)
            .addTopLevelElement("state", state)
            .addTopLevelElement("redirectUri", redirectUri)
            .addTopLevelElement("landingPage", landingPage)
            .addTopLevelElement("loginOnly", String.valueOf(loginOnly));

        if (isSignupOnly()) {
            builder.addTopLevelElement("signupOnly", "true");
        }

        for (String paymentMethod : paymentMethods) {
            builder.addTopLevelElement("payment_methods[]", paymentMethod);
        }

        builder.addElement("user", user)
            .addElement("business", business);

        return builder.toQueryString();
    }
}
