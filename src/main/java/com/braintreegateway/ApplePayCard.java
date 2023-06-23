package com.braintreegateway;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.braintreegateway.util.NodeWrapper;

public class ApplePayCard implements PaymentMethod {
    private Address billingAddress;
    private String bin;
    private String cardholderName;
    private String cardType;
    private String commercial;
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
    private String issuingBank;
    private String last4;
    private String merchantTokenIdentifier;
    private String paymentInstrumentName;
    private String payroll;
    private String prepaid;
    private String productId;
    private String sourceCardLast4;
    private String sourceDescription;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;

    public ApplePayCard(NodeWrapper node) {
        this.bin = node.findString("bin");
        this.cardholderName = node.findString("cardholder-name");
        this.cardType = node.findString("card-type");
        this.commercial = node.findString("commercial");
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
        this.issuingBank = node.findString("issuing-bank");
        this.last4 = node.findString("last-4");
        this.merchantTokenIdentifier = node.findString("merchant-token-identifier");
        this.paymentInstrumentName = node.findString("payment-instrument-name");
        this.payroll = node.findString("payroll");
        this.prepaid = node.findString("prepaid");
        this.productId = node.findString("product-id");
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

    public String getCommercial() {
        return commercial;
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

    public String getProductId() {
        return productId;
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

    public boolean isDefault() {
        return isDefault;
    }
}
