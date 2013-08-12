package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class WebhookNotification {
    public enum Kind {
        SUB_MERCHANT_ACCOUNT_APPROVED("sub_merchant_account_approved"),
        SUB_MERCHANT_ACCOUNT_DECLINED("sub_merchant_account_declined"),
        SUBSCRIPTION_CANCELED("subscription_canceled"),
        SUBSCRIPTION_CHARGED_SUCCESSFULLY("subscription_charged_successfully"),
        SUBSCRIPTION_CHARGED_UNSUCCESSFULLY("subscription_charged_unsuccessfully"),
        SUBSCRIPTION_EXPIRED("subscription_expired"),
        SUBSCRIPTION_TRIAL_ENDED("subscription_trial_ended"),
        SUBSCRIPTION_WENT_ACTIVE("subscription_went_active"),
        SUBSCRIPTION_WENT_PAST_DUE("subscription_went_past_due"),
        TRANSACTION_DISBURSED("transaction_disbursed"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private ValidationErrors errors;
    private MerchantAccount merchantAccount;
    private Subscription subscription;
    private Transaction transaction;
    private Kind kind;
    private Calendar timestamp;

    public WebhookNotification(NodeWrapper node) {
        this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"));
        this.timestamp = node.findDateTime("timestamp");

        NodeWrapper wrapperNode = node.findFirst("subject");

        if (wrapperNode.findFirst("api-error-response") != null) {
            wrapperNode = wrapperNode.findFirst("api-error-response");
        }

        if (wrapperNode.findFirst("subscription") != null) {
            this.subscription = new Subscription(wrapperNode.findFirst("subscription"));
        }

        if (wrapperNode.findFirst("merchant-account") != null) {
            this.merchantAccount = new MerchantAccount(wrapperNode.findFirst("merchant-account"));
        }

        if (wrapperNode.findFirst("transaction") != null) {
            this.transaction = new Transaction(wrapperNode.findFirst("transaction"));
        }

        if (!wrapperNode.isSuccess()) {
            this.errors = new ValidationErrors(wrapperNode);
        }
    }

    public ValidationErrors getErrors() {
        return this.errors;
    }

    public Kind getKind() {
        return this.kind;
    }

    public MerchantAccount getMerchantAccount() {
        return this.merchantAccount;
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public Calendar getTimestamp() {
        return this.timestamp;
    }
}
