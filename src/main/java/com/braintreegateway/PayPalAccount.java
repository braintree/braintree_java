package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class PayPalAccount implements PaymentMethod {
    private String email;
    private String token;
    private boolean isDefault;
    private String imageUrl;
    private Calendar createdAt;
    private Calendar updatedAt;

    public PayPalAccount(NodeWrapper node) {
        this.email = node.findString("email");
        this.token = node.findString("token");
        this.isDefault = node.findBoolean("default");
        this.imageUrl = node.findString("image-url");
        this.imageUrl = node.findString("image-url");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }
}
