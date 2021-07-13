package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApplePayCard implements PaymentMethod {
    private String imageUrl;
    private String token;
    private boolean isDefault;
    private String bin;
    private String cardType;
    private String paymentInstrumentName;
    private String sourceDescription;
    private String last4;
    private String expirationMonth;
    private String expirationYear;
    private boolean expired;
    private String customerId;
    private String cardholderName;
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;
    private String prepaid;
    private String healthcare;
    private String debit;
    private String durbinRegulated;
    private String commercial;
    private String payroll;
    private String issuingBank;
    private String countryOfIssuance;
    private String productId;

    public ApplePayCard(NodeWrapper node) {
        this.token = node.findString("token");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.bin = node.findString("bin");
        this.cardType = node.findString("card-type");
        this.paymentInstrumentName = node.findString("payment-instrument-name");
        this.sourceDescription = node.findString("source-description");
        this.last4 = node.findString("last-4");
        this.expirationMonth = node.findString("expiration-month");
        this.expirationYear = node.findString("expiration-year");
        this.expired = node.findBoolean("expired");
        this.customerId = node.findString("customer-id");
        this.cardholderName = node.findString("cardholder-name");
        this.createdAt = node.findDateTime("created-at");
        this.updatedAt = node.findDateTime("updated-at");
        this.subscriptions = new ArrayList<Subscription>();
        this.prepaid = node.findString("prepaid");
        this.healthcare = node.findString("healthcare");
        this.debit = node.findString("debit");
        this.durbinRegulated = node.findString("durbin-regulated");
        this.commercial = node.findString("commercial");
        this.payroll = node.findString("payroll");
        this.issuingBank = node.findString("issuing-bank");
        this.countryOfIssuance = node.findString("country-of-issuance");
        this.productId = node.findString("product-id");
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }

    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getBin() {
        return bin;
    }

    public String getCardType() {
        return cardType;
    }

    public String getPaymentInstrumentName() {
        return paymentInstrumentName;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public boolean getExpired() {
        return expired;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public String getLast4() {
        return last4;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public String getHealthcare() {
        return healthcare;
    }

    public String getDebit() {
        return debit;
    }

    public String getDurbinRegulated() {
        return durbinRegulated;
    }

    public String getCommercial() {
        return commercial;
    }

    public String getPayroll() {
        return payroll;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public String getCountryOfIssuance() {
        return countryOfIssuance;
    }

    public String getProductId() {
        return productId;
    }
}
