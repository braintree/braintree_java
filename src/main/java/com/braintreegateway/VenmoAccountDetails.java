package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class VenmoAccountDetails {

    private String token;
    private String username;
    private String venmoUserId;
    private String imageUrl;
    private String sourceDescription;

    public VenmoAccountDetails(NodeWrapper node) {
        this.token = node.findString("token");
        this.username = node.findString("username");
        this.venmoUserId = node.findString("venmo-user-id");
        this.imageUrl = node.findString("image-url");
        this.sourceDescription = node.findString("source-description");
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getVenmoUserId() {
        return venmoUserId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

}
