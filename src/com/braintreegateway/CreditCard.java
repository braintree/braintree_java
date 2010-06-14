package com.braintreegateway;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

public class CreditCard {

    public enum CardType {
        AMEX("American Express"),
        CARTE_BLANCHE("Carte Blanche"),
        CHINA_UNION_PAY("China UnionPay"),
        DINERS_CLUB_INTERNATIONAL("Diners Club"),
        DISCOVER("Discover"),
        JCB("JCB"),
        LASER("Laser"),
        MAESTRO("Maestro"),
        MASTER_CARD("MasterCard"),
        SOLO("Solo"),
        SWITCH("Switch"),
        VISA("Visa");
        
        private final String name;
        
        CardType(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    }
    
    public enum CustomerLocation {
        US("us"),
        INTERNATIONAL("international");
        
        private final String name;
        
        CustomerLocation(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    }
    
    private Address billingAddress;
    private String bin;
    private String cardholderName;
    private String cardType;
    private Calendar createdAt;
    private String customerId;
    private String customerLocation;
    private String expirationMonth;
    private String expirationYear;
    private boolean isDefault;
    private String last4;
    private String token;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;

    public CreditCard(NodeWrapper node) {
        token = node.findString("token");
        createdAt = node.findDateTime("created-at");
        updatedAt = node.findDateTime("updated-at");
        bin = node.findString("bin");
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        customerId = node.findString("customer-id");
        customerLocation = node.findString("customer-location");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        isDefault = node.findBoolean("default");
        last4 = node.findString("last-4");
        token = node.findString("token");
        NodeWrapper billingAddressResponse = node.findFirst("billing-address");
        if (billingAddressResponse != null) {
            billingAddress = new Address(billingAddressResponse);
        }
        subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            subscriptions.add(new Subscription(subscriptionResponse));
        }
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getBin() {
        return bin;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getCardType() {
        return cardType;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public String getExpirationDate() {
        return expirationMonth + "/" + expirationYear;
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

    public String getMaskedNumber() {
        return getBin() + "******" + getLast4();
    }

    public String getToken() {
        return token;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
