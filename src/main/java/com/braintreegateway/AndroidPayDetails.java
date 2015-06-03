package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class AndroidPayDetails {
    private String cardType;
    private String last4;
    private String sourceCardType;
    private String sourceCardLast4;
    private String virtualCardType;
    private String virtualCardLast4;
    private String expirationMonth;
    private String expirationYear;
    private String token;
    private String googleTransactionId;
    private String bin;
    private String imageUrl;

    public AndroidPayDetails(NodeWrapper node) {
        this.sourceCardType = node.findString("source-card-type");
        this.sourceCardLast4 = node.findString("source-card-last-4");
        this.virtualCardType = node.findString("virtual-card-type");
        this.virtualCardLast4 = node.findString("virtual-card-last-4");
        this.cardType = this.virtualCardType;
        this.last4 = this.virtualCardLast4;
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.token = node.findString("token");
        this.googleTransactionId = node.findString("google-transaction-id");
        this.bin = node.findString("bin");
        this.imageUrl = node.findString("image-url");
    }

    public String getCardType() {
        return cardType;
    }

    public String getLast4() {
        return last4;
    }

    public String getSourceCardType() {
        return sourceCardType;
    }

    public String getSourceCardLast4() {
        return sourceCardLast4;
    }

    public String getVirtualCardType() {
        return virtualCardType;
    }

    public String getVirtualCardLast4() {
        return virtualCardLast4;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getToken() {
        return token;
    }

    public String getGoogleTransactionId() {
        return googleTransactionId;
    }

    public String getBin() {
        return bin;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
