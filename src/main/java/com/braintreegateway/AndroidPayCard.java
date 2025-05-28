package com.braintreegateway;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.braintreegateway.util.EnumUtils.findByToString;

import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.util.NodeWrapper;

// NEXT_MAJOR_VERSION - rename this to GooglePayCard
public class AndroidPayCard implements PaymentMethod {
    private Address billingAddress;
    private String bin;
    private String business;
    private String cardType;
    private String commercial;
    private String consumer;
    private String corporate;
    private String countryOfIssuance;
    private Calendar createdAt;
    private String customerId;
    private String debit;
    private String durbinRegulated;
    private String expirationMonth;
    private String expirationYear;
    private String googleTransactionId;
    private String healthcare;
    private String imageUrl;
    private Boolean isDefault;
    private Boolean isNetworkTokenized;
    private String issuingBank;
    private String last4;
    private String payroll;
    private String prepaid;
    private String prepaidReloadable;
    private String productId;
    private String purchase;
    private String sourceCardLast4;
    private String sourceCardType;
    private String sourceDescription;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;
    private String virtualCardType;
    private String virtualCardLast4;

    public AndroidPayCard(NodeWrapper node) {
        this.bin = node.findString("bin");
        this.business = node.findString("business");
        this.commercial = node.findString("commercial");
        this.consumer = node.findString("consumer");
        this.corporate = node.findString("corporate");
        this.countryOfIssuance = node.findString("country-of-issuance");
        this.createdAt = node.findDateTime("created-at");
        this.customerId = node.findString("customer-id");
        this.debit = node.findString("debit");
        this.durbinRegulated = node.findString("durbin-regulated");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.googleTransactionId = node.findString("google-transaction-id");
        this.healthcare = node.findString("healthcare");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.isNetworkTokenized = node.findBoolean("is-network-tokenized");
        this.issuingBank = node.findString("issuing-bank");
        this.payroll = node.findString("payroll");
        this.prepaid = node.findString("prepaid");
        this.prepaidReloadable = node.findString("prepaid-reloadable");
        this.productId = node.findString("product-id");
        this.purchase = node.findString("purchase");
        this.sourceCardType = node.findString("source-card-type");
        this.sourceDescription = node.findString("source-description"); 
        this.sourceCardLast4 = node.findString("source-card-last-4");
        this.token = node.findString("token");
        this.updatedAt = node.findDateTime("updated-at");
        this.virtualCardLast4 = node.findString("virtual-card-last-4");
        this.virtualCardType = node.findString("virtual-card-type");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        NodeWrapper billingAddressResponse = node.findFirst("billing-address");
        if (billingAddressResponse != null) {
            this.billingAddress = new Address(billingAddressResponse);
        }
        // These setters are reliant on virtualCardType and virtualCardLast4 set above
        this.cardType = this.virtualCardType;
        this.last4 = this.virtualCardLast4;

    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getBin() {
        return bin;
    }

    public Business getBusiness() {
        return findByToString(Business.values(), business, Business.UNKNOWN);
    }

    public String getCardType() {
        return cardType;
    }

    public String getCommercial() {
        return commercial;
    }

    public Consumer getConsumer() {
        return findByToString(Consumer.values(), consumer, Consumer.UNKNOWN);
    }

    public Corporate getCorporate() {
        return findByToString(Corporate.values(), corporate, Corporate.UNKNOWN);
    }

    public String getCountryOfIssuance() {
        return countryOfIssuance;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDebit() {
        return debit;
    }

    public String getDurbinRegulated() {
        return durbinRegulated;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getGoogleTransactionId() {
        return googleTransactionId;
    }

    public String getHealthcare() {
        return healthcare;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public String getLast4() {
        return last4;
    }

    public String getPayroll() {
        return payroll;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public PrepaidReloadable getPrepaidReloadable() {
        return findByToString(PrepaidReloadable.values(), prepaidReloadable, PrepaidReloadable.UNKNOWN);
    }

    public String getProductId() {
        return productId;
    }

    public Purchase getPurchase() {
        return findByToString(Purchase.values(), purchase, Purchase.UNKNOWN);
    }

    public String getSourceCardLast4() {
        return sourceCardLast4;
    }

    public String getSourceCardType() {
        return sourceCardType;
    }

    public String getSourceDescription() {
        return sourceDescription;
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

    public String getVirtualCardLast4() {
        return virtualCardLast4;
    }

    public String getVirtualCardType() {
        return virtualCardType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isNetworkTokenized() {
        return isNetworkTokenized;
    }
}
