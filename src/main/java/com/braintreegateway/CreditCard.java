package com.braintreegateway;

import static com.braintreegateway.util.EnumUtils.findByToString;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;

// NEXT_MAJOR_VERSION remove isVenmoSDK
// The old venmo SDK integration has been deprecated
public class CreditCard implements PaymentMethod {
    public static final String VALUE_YES = "Yes";
    public static final String VALUE_NO = "No";
    public static final String VALUE_UNKNOWN = "Unknown";

    public enum CardType {
        AMEX("American Express"),
        CARTE_BLANCHE("Carte Blanche"),
        CHINA_UNION_PAY("China UnionPay"),
        DINERS_CLUB_INTERNATIONAL("Diners Club"),
        DISCOVER("Discover"),
        ELO("Elo"),
        JCB("JCB"),
        LASER("Laser"),
        UK_MAESTRO("UK Maestro"),
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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

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
        YES(VALUE_YES),
        NO(VALUE_NO),
        UNKNOWN(VALUE_UNKNOWN);

        private final String value;

        Prepaid(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum DebitNetwork {
        ACCEL,
        MAESTRO,
        NYCE,
        PULSE,
        STAR,
        STAR_ACCESS;
    }

    private String accountType;
    private Address billingAddress;
    private String bin;
    private String binExtended;
    private String business;
    private String cardholderName;
    private String cardType;
    private String commercial;
    private String consumer;
    private String corporate;
    private String countryOfIssuance;
    private Calendar createdAt;
    private String customerId;
    private String customerLocation;
    private String debit;
    private String durbinRegulated;
    private String expirationMonth;
    private String expirationYear;
    private String healthcare;
    private String imageUrl;
    private boolean isDefault;
    private boolean isExpired;
    private boolean isNetworkTokenized;
    private boolean isVenmoSdk;
    private String issuingBank;
    private String last4;
    private String payroll;
    private String prepaid;
    private String prepaidReloadable;
    private String productId;
    private String purchase;
    private List<Subscription> subscriptions;
    private String token;
    private String uniqueNumberIdentifier;
    private Calendar updatedAt;
    private CreditCardVerification verification;

    public CreditCard(NodeWrapper node) {
        accountType = node.findString("account-type");

        NodeWrapper billingAddressResponse = node.findFirst("billing-address");
        if (billingAddressResponse != null) {
            billingAddress = new Address(billingAddressResponse);
        }

        bin = node.findString("bin");
        binExtended = node.findString("bin-extended");
        business = node.findString("business");
        cardholderName = node.findString("cardholder-name");
        cardType = node.findString("card-type");
        commercial = node.findString("commercial");
        consumer = node.findString("consumer");
        corporate = node.findString("corporate");
        countryOfIssuance = node.findString("country-of-issuance");
        createdAt = node.findDateTime("created-at");
        customerId = node.findString("customer-id");
        customerLocation = node.findString("customer-location");
        debit = node.findString("debit");
        durbinRegulated = node.findString("durbin-regulated");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        healthcare = node.findString("healthcare");
        imageUrl = node.findString("image-url");
        isDefault = node.findBoolean("default");
        isExpired = node.findBoolean("expired");
        isNetworkTokenized = node.findBoolean("is-network-tokenized");
        issuingBank = node.findString("issuing-bank");
        isVenmoSdk = node.findBoolean("venmo-sdk");
        last4 = node.findString("last-4");
        payroll = node.findString("payroll");
        prepaid = node.findString("prepaid");
        prepaidReloadable = node.findString("prepaid-reloadable");
        productId = node.findString("product-id");
        purchase = node.findString("purchase");

        subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            subscriptions.add(new Subscription(subscriptionResponse));
        }

        token = node.findString("token");
        uniqueNumberIdentifier = node.findString("unique-number-identifier");
        updatedAt = node.findDateTime("updated-at");

        final List<NodeWrapper> verificationNodes = node.findAll("verifications/verification");
        verification = findNewestVerification(verificationNodes);
    }

    private CreditCardVerification findNewestVerification(List<NodeWrapper> verificationNodes) {
        if (verificationNodes.size() > 0) {
            Collections.sort(verificationNodes, new Comparator<NodeWrapper>() {
                public int compare(NodeWrapper node1, NodeWrapper node2) {
                    Calendar createdAt1 = node1.findDateTime("created-at");
                    Calendar createdAt2 = node2.findDateTime("created-at");

                    return createdAt2.compareTo(createdAt1);
                }
            });

            return new CreditCardVerification(verificationNodes.get(0));
        }

        return null;
    }

    public String getAccountType() {
        return accountType;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getBin() {
        return bin;
    }

    public String getBinExtended() {
        return binExtended;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getCardType() {
        return cardType;
    }

    public Business getBusiness() {
        return findByToString(Business.values(), business, Business.UNKNOWN);
    }

    public Commercial getCommercial() {
        return findByToString(Commercial.values(), commercial, Commercial.UNKNOWN);
    }

    public Consumer getConsumer() {
        return findByToString(Consumer.values(), consumer, Consumer.UNKNOWN);
    }

    public Corporate getCorporate() {
        return findByToString(Corporate.values(), corporate, Corporate.UNKNOWN);
    }

    public String getCountryOfIssuance() {
        if ("".equals(countryOfIssuance)) {
            return "Unknown";
        } else {
            return countryOfIssuance;
        }
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

    public Debit getDebit() {
        return findByToString(Debit.values(), debit, Debit.UNKNOWN);
    }

    public DurbinRegulated getDurbinRegulated() {
        return findByToString(DurbinRegulated.values(), durbinRegulated, DurbinRegulated.UNKNOWN);
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

    public Healthcare getHealthcare() {
        return findByToString(Healthcare.values(), healthcare, Healthcare.UNKNOWN);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIssuingBank() {
        if ("".equals(issuingBank)) {
            return "Unknown";
        } else {
            return issuingBank;
        }
    }

    public String getLast4() {
        return last4;
    }

    public String getMaskedNumber() {
        return getBin() + "******" + getLast4();
    }

    public Payroll getPayroll() {
        return findByToString(Payroll.values(), payroll, Payroll.UNKNOWN);
    }

    public Prepaid getPrepaid() {
        return findByToString(Prepaid.values(), prepaid, Prepaid.UNKNOWN);
    }

    public PrepaidReloadable getPrepaidReloadable() {
        return findByToString(PrepaidReloadable.values(), prepaidReloadable, PrepaidReloadable.UNKNOWN);
    }

    public String getProductId() {
        if ("".equals(productId)) {
            return "Unknown";
        } else {
            return productId;
        }
    }

    public Purchase getPurchase() {
        return findByToString(Purchase.values(), purchase, Purchase.UNKNOWN);
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public String getToken() {
        return token;
    }

    public String getUniqueNumberIdentifier() {
        return uniqueNumberIdentifier;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public CreditCardVerification getVerification() {
        return verification;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public boolean isNetworkTokenized() {
        return isNetworkTokenized;
    }

    //NEXT_MAJOR_VERSION remove this method
    /**
     * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
    */
    @Deprecated
    public boolean isVenmoSdk() {
      return isVenmoSdk;
    }
}
