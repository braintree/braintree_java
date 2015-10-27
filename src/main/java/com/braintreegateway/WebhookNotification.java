package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class WebhookNotification {
    public enum Kind {
        CHECK("check"),
        PARTNER_MERCHANT_DISCONNECTED("partner_merchant_disconnected"),
        PARTNER_MERCHANT_CONNECTED("partner_merchant_connected"),
        PARTNER_MERCHANT_DECLINED("partner_merchant_declined"),
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
        DISBURSEMENT_EXCEPTION("disbursement_exception"),
        DISBURSEMENT("disbursement"),
        DISPUTE_OPENED("dispute_opened"),
        DISPUTE_LOST("dispute_lost"),
        DISPUTE_WON("dispute_won"),
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
    private Disbursement disbursement;
    private Dispute dispute;
    private Kind kind;
    private Calendar timestamp;
    private PartnerMerchant partnerMerchant;

    public WebhookNotification(NodeWrapper node) {
        this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        this.timestamp = node.findDateTime("timestamp");

        NodeWrapper wrapperNode = node.findFirst("subject");

        if (wrapperNode.findFirst("api-error-response") != null) {
            wrapperNode = wrapperNode.findFirst("api-error-response");
        }

        if (wrapperNode.findFirst("subscription") != null) {
            this.subscription = new Subscription(wrapperNode.findFirst("subscription"));
        }

        if (wrapperNode.findFirst("disbursement") != null) {
            this.disbursement = new Disbursement(wrapperNode.findFirst("disbursement"));
        }

        if (wrapperNode.findFirst("merchant-account") != null) {
            this.merchantAccount = new MerchantAccount(wrapperNode.findFirst("merchant-account"));
        }

        if (wrapperNode.findFirst("dispute") != null) {
            this.dispute = new Dispute(wrapperNode.findFirst("dispute"));
        }

        if (wrapperNode.findFirst("transaction") != null) {
            this.transaction = new Transaction(wrapperNode.findFirst("transaction"));
        }

        if (wrapperNode.findFirst("partner-merchant") != null) {
            this.partnerMerchant = new PartnerMerchant(wrapperNode.findFirst("partner-merchant"));
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

    public Disbursement getDisbursement() {
        return this.disbursement;
    }

    public Dispute getDispute() {
        return this.dispute;
    }

    public Calendar getTimestamp() {
        return this.timestamp;
    }

    public PartnerMerchant getPartnerMerchant() {
        return this.partnerMerchant;
    }
}
