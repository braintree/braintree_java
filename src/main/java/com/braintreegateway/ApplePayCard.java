package com.braintreegateway;

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
import static com.braintreegateway.util.EnumUtils.findByToString;
import com.braintreegateway.util.NodeWrapper;

public class ApplePayCard implements PaymentMethod {
    private Address billingAddress;
    private String bin;
    private String business;
    private String cardholderName;
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
    private boolean expired;
    private String healthcare;
    private String imageUrl;
    private boolean isDefault;
    private boolean isDeviceToken;
    private String issuingBank;
    private String last4;
    private String merchantTokenIdentifier;
    private String paymentInstrumentName;
    private String payroll;
    private String prepaid;
    private String prepaidReloadable;
    private String productId;
    private String purchase;
    private String sourceCardLast4;
    private String sourceDescription;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;
    private CreditCardVerification verification;

    public ApplePayCard(NodeWrapper node) {
        this.bin = node.findString("bin");
        this.business = node.findString("business");
        this.cardholderName = node.findString("cardholder-name");
        this.cardType = node.findString("card-type");
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
        this.expired = node.findBoolean("expired");
        this.healthcare = node.findString("healthcare");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.isDeviceToken = node.findBoolean("is-device-token");
        this.issuingBank = node.findString("issuing-bank");
        this.last4 = node.findString("last-4");
        this.merchantTokenIdentifier = node.findString("merchant-token-identifier");
        this.paymentInstrumentName = node.findString("payment-instrument-name");
        this.payroll = node.findString("payroll");
        this.prepaid = node.findString("prepaid");
        this.prepaidReloadable = node.findString("prepaid-reloadable");
        this.productId = node.findString("product-id");
        this.purchase = node.findString("purchase");
        this.sourceCardLast4 = node.findString("source-card-last4");
        this.sourceDescription = node.findString("source-description");
        this.subscriptions = new ArrayList<Subscription>();
        this.token = node.findString("token");
        this.updatedAt = node.findDateTime("updated-at");

        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        NodeWrapper billingAddressResponse = node.findFirst("billing-address");
        if (billingAddressResponse != null) {
            this.billingAddress = new Address(billingAddressResponse);
        }

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

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getBin() {
        return bin;
    }

    public Business getBusiness() {
        return findByToString(Business.values(), business, Business.UNKNOWN);
    }

    public String getCardholderName() {
        return cardholderName;
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

    public String getCustomerId() {
        return customerId;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getDebit() {
        return debit;
    }

    public String getDurbinRegulated() {
        return durbinRegulated;
    }

    public boolean getExpired() {
        return expired;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public String getHealthcare() {
        return healthcare;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getIsDeviceToken(){
        return isDeviceToken;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public String getLast4() {
        return last4;
    }

    public String getMerchantTokenIdentifier() {
        return merchantTokenIdentifier;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
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

    public CreditCardVerification getVerification() {
        return verification;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
