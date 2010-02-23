package com.braintreegateway;

import java.util.Calendar;

import com.braintreegateway.util.NodeWrapper;

public class CreditCard {

    private Calendar createdAt;
    private Calendar updatedAt;
    private String bin;
    private String cardholderName;
    private String cardType;
    private String expirationMonth;
    private String expirationYear;
    private String last4;
    private String token;
    private Address billingAddress;

    public CreditCard(NodeWrapper node) {
        token = node.findString("token");
        createdAt = node.findDateTime("created-at");
        updatedAt = node.findDateTime("updated-at");
        bin = node.findString("bin");
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        last4 = node.findString("last-4");
        token = node.findString("token");
        NodeWrapper billingAddressResponse = node.findFirst("billing-address");
        if (billingAddressResponse != null) {
            billingAddress = new Address(billingAddressResponse);
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public String getToken() {
        return token;
    }

    public String getCardholderName() {
        return cardholderName;
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

    public String getLast4() {
        return last4;
    }

    public String getBin() {
        return bin;
    }

    public String getExpirationDate() {
        return expirationMonth + "/" + expirationYear;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

}
