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

    public enum Commercial {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        Commercial(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Debit {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        Debit(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum DurbinRegulated {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        DurbinRegulated(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Healthcare {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        Healthcare(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Payroll {
        YES("Yes"),
        NO("No"),
        UNKNOWN("Unknown");

        private final String value;

        Payroll(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
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
    private boolean isVenmoSdk;
    private boolean isExpired;
    private String imageUrl;
    private String last4;
    private String commercial;
    private String debit;
    private String durbinRegulated;
    private String healthcare;
    private String payroll;
    private String prepaid;
    private String countryOfIssuance;
    private String issuingBank;
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
        imageUrl = node.findString("image-url");
        isDefault = node.findBoolean("default");
        isVenmoSdk = node.findBoolean("venmo-sdk");
        isExpired = node.findBoolean("expired");
        last4 = node.findString("last-4");
        commercial = node.findString("commercial");
        debit = node.findString("debit");
        durbinRegulated = node.findString("durbin-regulated");
        healthcare = node.findString("healthcare");
        payroll = node.findString("payroll");
        prepaid = node.findString("prepaid");
        countryOfIssuance = node.findString("country-of-issuance");
        issuingBank = node.findString("issuing-bank");
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLast4() {
        return last4;
    }

    public String getMaskedNumber() {
        return getBin() + "******" + getLast4();
    }

    public Commercial getCommercial() {
      if(commercial.equals(Commercial.YES.toString())) {
        return Commercial.YES;
      } else if (commercial.equals(Commercial.NO.toString())) {
        return Commercial.NO;
      } else {
        return Commercial.UNKNOWN;
      }
    }

    public Debit getDebit() {
      if(debit.equals(Debit.YES.toString())) {
        return Debit.YES;
      } else if (debit.equals(Debit.NO.toString())) {
        return Debit.NO;
      } else {
        return Debit.UNKNOWN;
      }
    }

    public DurbinRegulated getDurbinRegulated() {
      if(durbinRegulated.equals(DurbinRegulated.YES.toString())) {
        return DurbinRegulated.YES;
      } else if (durbinRegulated.equals(DurbinRegulated.NO.toString())) {
        return DurbinRegulated.NO;
      } else {
        return DurbinRegulated.UNKNOWN;
      }
    }

    public Healthcare getHealthcare() {
      if(healthcare.equals(Healthcare.YES.toString())) {
        return Healthcare.YES;
      } else if (healthcare.equals(Healthcare.NO.toString())) {
        return Healthcare.NO;
      } else {
        return Healthcare.UNKNOWN;
      }
    }

    public Payroll getPayroll() {
      if(payroll.equals(Payroll.YES.toString())) {
        return Payroll.YES;
      } else if (payroll.equals(Payroll.NO.toString())) {
        return Payroll.NO;
      } else {
        return Payroll.UNKNOWN;
      }
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

    public String getCountryOfIssuance() {
        if(countryOfIssuance.equals("")) {
            return "Unknown";
        } else {
            return countryOfIssuance;
        }
    }

    public String getIssuingBank() {
        if(issuingBank.equals("")) {
            return "Unknown";
        } else {
            return issuingBank;
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

    public boolean isVenmoSdk() {
      return isVenmoSdk;
    }

    public boolean isExpired() {
        return isExpired;
    }
}
