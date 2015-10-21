package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class AmexExpressCheckoutDetails {

    private String cardType;
    private String token;
    private String bin;
    private String expirationMonth;
    private String expirationYear;
    private String cardMemberNumber;
    private String cardMemberExpiryDate;
    private String imageUrl;
    private String sourceDescription;

    public AmexExpressCheckoutDetails(NodeWrapper node) {
        this.bin = node.findString("bin");
        this.cardMemberExpiryDate = node.findString("card-member-expiry-date");
        this.cardMemberNumber = node.findString("card-member-number");
        this.cardType = node.findString("card-type");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.imageUrl = node.findString("image-url");
        this.sourceDescription = node.findString("source-description");
        this.token = node.findString("token");
    }

    public String getBin() {
        return bin;
    }

    public String getCardMemberExpiryDate() {
        return cardMemberExpiryDate;
    }

    public String getCardMemberNumber() {
        return cardMemberNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getToken() {
        return token;
    }
}
