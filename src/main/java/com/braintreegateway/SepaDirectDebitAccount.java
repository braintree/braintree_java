package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.SepaDirectDebitAccountDetails.MandateType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SepaDirectDebitAccount implements PaymentMethod {
    private Calendar createdAt;
    private Calendar updatedAt;
    private List<Subscription> subscriptions;
    private MandateType mandateType;
    private String bankReferenceToken;
    private String customerGlobalId;
    private String customerId;
    private String globalId;
    private String imageUrl;
    private String last4;
    private String merchantAccountId;
    private String merchantOrPartnerCustomerId;
    private String token;
    private String viewMandateUrl;
    private boolean isDefault;

    public SepaDirectDebitAccount(NodeWrapper node) {
        bankReferenceToken = node.findString("bank-reference-token");
        createdAt = node.findDateTime("created-at");
        customerGlobalId = node.findString("customer-global-id");
        customerId = node.findString("customer-id");
        merchantOrPartnerCustomerId = node.findString("merchant-or-partner-customer-id");
        globalId = node.findString("global-id");
        last4 = node.findString("last-4");
        imageUrl = node.findString("image-url");
        isDefault = node.findBoolean("default");
        mandateType = EnumUtils.findByName(MandateType.class, node.findString("mandate-type"), MandateType.ONE_OFF);
        merchantAccountId = node.findString("merchant-account-id");
        subscriptions = new ArrayList<Subscription>();
        for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
            subscriptions.add(new Subscription(subscriptionResponse));
        }
        token = node.findString("token");
        updatedAt = node.findDateTime("updated-at");
        viewMandateUrl = node.findString("view-mandate-url");
    }

    public MandateType getMandateType() {
        return mandateType;
    }

    public String getBankReferenceToken() {
        return bankReferenceToken;
    }

    public String getMerchantOrPartnerCustomerId() {
        return merchantOrPartnerCustomerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerGlobalId() {
        return customerGlobalId;
    }

    public String getGlobalId() {
        return globalId;
    }

    public String getLast4() {
        return last4;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getViewMandateUrl() {
        return viewMandateUrl;
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getToken() {
        return token;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }
}
