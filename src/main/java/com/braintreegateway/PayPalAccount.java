package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PayPalAccount implements PaymentMethod {
    private String billingAgreementId;
    private Calendar createdAt;
    private String customerId;
    private String editPaypalVaultId;
    private String email;
    private String fundingSourceDescription;
    private String imageUrl;
    private boolean isDefault;
    private String payerId;
    private Calendar revokedAt;
    private List<Subscription> subscriptions;
    private String token;
    private Calendar updatedAt;

    public PayPalAccount(NodeWrapper node) {
        this.billingAgreementId = node.findString("billing-agreement-id");
        this.createdAt = node.findDateTime("created-at");
        this.customerId = node.findString("customer-id");
        this.editPaypalVaultId = node.findString("edit-paypal-vault-id");
        this.email = node.findString("email");
        this.fundingSourceDescription = node.findString("funding-source-description");
        this.imageUrl = node.findString("image-url");
        this.isDefault = node.findBoolean("default");
        this.payerId = node.findString("payer-id");
        this.revokedAt = node.findDateTime("revoked-at");
        this.subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            this.subscriptions.add(new Subscription(subscriptionResponse));
        }
        this.token = node.findString("token");
        this.updatedAt = node.findDateTime("updated-at");
    }

    public String getBillingAgreementId() {
        return billingAgreementId;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }
    
    public String getCustomerId() {
        return customerId;
    }

    public String getEditPaypalVaultId() {
        return editPaypalVaultId;
    }

    public String getEmail() {
        return email;
    }

    public String getFundingSourceDescription() {
        return fundingSourceDescription;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getToken() {
        return token;
    }

    public Calendar getRevokedAt() {
        return revokedAt;
    }
    
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
    
    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDefault() {
        return isDefault;
    }

}
