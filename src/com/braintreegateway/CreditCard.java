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

        @Override
        public String toString() {
            return name;
        }
    }

    public enum CustomerLocation {
        INTERNATIONAL("international"),
        US("us");

        private final String name;

        CustomerLocation(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Prepaid {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        Prepaid(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
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
    private boolean isExpired;
    private String last4;
    private String prepaid;
    private String uniqueNumberIdentifier;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;

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
        isExpired = node.findBoolean("expired");
        last4 = node.findString("last-4");
        prepaid = node.findString("prepaid");
        uniqueNumberIdentifier = node.findString("unique-number-identifier");
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

    public Prepaid getPrepaid() {
      if(prepaid.equals(Prepaid.YES.toString())) {
        return Prepaid.YES;
      } else if (prepaid.equals(Prepaid.NO.toString())) {
        return Prepaid.NO;
      } else {
        return Prepaid.UNKNOWN;
      }
    }

    public String getUniqueNumberIdentifier() {
        return uniqueNumberIdentifier;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public String getToken() {
        return token;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isExpired() {
        return isExpired;
    }
}
