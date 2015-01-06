package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class CoinbaseDetails {
    private String userEmail;
    private String userName;
    private String token;
    private String imageUrl;

    public CoinbaseDetails(NodeWrapper node) {
        this.userEmail = node.findString("user-email");
        this.userName = node.findString("user-name");
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
