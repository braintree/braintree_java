package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class WebhookNotification {
    public enum Kind {
        ACCOUNT_UPDATER_DAILY_REPORT("account_updater_daily_report"),
        CHECK("check"),
        CONNECTED_MERCHANT_STATUS_TRANSITIONED("connected_merchant_status_transitioned"),
        CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED("connected_merchant_paypal_status_changed"),
        DISBURSEMENT("disbursement"),
        DISBURSEMENT_EXCEPTION("disbursement_exception"),
        DISPUTE_ACCEPTED("dispute_accepted"),
        DISPUTE_AUTO_ACCEPTED("dispute_auto_accepted"),
        DISPUTE_DISPUTED("dispute_disputed"),
        DISPUTE_EXPIRED("dispute_expired"),
        DISPUTE_LOST("dispute_lost"),
        DISPUTE_OPENED("dispute_opened"),
        DISPUTE_WON("dispute_won"),
        GRANTED_PAYMENT_METHOD_REVOKED("granted_payment_method_revoked"),
        GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD("grantor_updated_granted_payment_method"),
        LOCAL_PAYMENT_COMPLETED("local_payment_completed"),
        LOCAL_PAYMENT_EXPIRED("local_payment_expired"),
        LOCAL_PAYMENT_FUNDED("local_payment_funded"),
        LOCAL_PAYMENT_REVERSED("local_payment_reversed"),
        OAUTH_ACCESS_REVOKED("oauth_access_revoked"),
        PARTNER_MERCHANT_DISCONNECTED("partner_merchant_disconnected"),
        PARTNER_MERCHANT_CONNECTED("partner_merchant_connected"),
        PARTNER_MERCHANT_DECLINED("partner_merchant_declined"),
        PAYMENT_METHOD_CUSTOMER_DATA_UPDATED("payment_method_customer_data_updated"),
        PAYMENT_METHOD_REVOKED_BY_CUSTOMER("payment_method_revoked_by_customer"),
        RECIPIENT_UPDATED_GRANTED_PAYMENT_METHOD("recipient_updated_granted_payment_method"),
        SUB_MERCHANT_ACCOUNT_APPROVED("sub_merchant_account_approved"),
        SUB_MERCHANT_ACCOUNT_DECLINED("sub_merchant_account_declined"),
        SUBSCRIPTION_BILLING_SKIPPED("subscription_billing_skipped"),
        SUBSCRIPTION_CANCELED("subscription_canceled"),
        SUBSCRIPTION_CHARGED_SUCCESSFULLY("subscription_charged_successfully"),
        SUBSCRIPTION_CHARGED_UNSUCCESSFULLY("subscription_charged_unsuccessfully"),
        SUBSCRIPTION_EXPIRED("subscription_expired"),
        SUBSCRIPTION_TRIAL_ENDED("subscription_trial_ended"),
        SUBSCRIPTION_WENT_ACTIVE("subscription_went_active"),
        SUBSCRIPTION_WENT_PAST_DUE("subscription_went_past_due"),
        TRANSACTION_DISBURSED("transaction_disbursed"),
        TRANSACTION_REVIEWED("transaction_reviewed"),
        TRANSACTION_SETTLED("transaction_settled"),
        TRANSACTION_SETTLEMENT_DECLINED("transaction_settlement_declined"),
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


    private AccountUpdaterDailyReport accountUpdaterDailyReport;
    private ConnectedMerchantPayPalStatusChanged connectedMerchantPayPalStatusChanged;
    private ConnectedMerchantStatusTransitioned connectedMerchantStatusTransitioned;
    private Disbursement disbursement;
    private Dispute dispute;
    private ValidationErrors errors;
    private GrantedPaymentInstrumentUpdate grantedPaymentInstrumentUpdate;
    private Kind kind;
    private LocalPaymentCompleted localPaymentCompleted;
    private LocalPaymentExpired localPaymentExpired;
    private LocalPaymentFunded localPaymentFunded;
    private LocalPaymentReversed localPaymentReversed;
    private MerchantAccount merchantAccount;
    private OAuthAccessRevocation oauthAccessRevocation;
    private PartnerMerchant partnerMerchant;
    private PaymentMethodCustomerDataUpdatedMetadata paymentMethodCustomerDataUpdatedMetadata;
    private RevokedPaymentMethodMetadata revokedPaymentMethodMetadata;
    private String sourceMerchantId;
    private Subscription subscription;
    private Calendar timestamp;
    private Transaction transaction;
    private TransactionReview transactionReview;

    public WebhookNotification(NodeWrapper node) {
        this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        this.timestamp = node.findDateTime("timestamp");

        this.sourceMerchantId = node.findString("source-merchant-id");

        NodeWrapper wrapperNode = node.findFirst("subject");

        NodeWrapper errorNode = wrapperNode.findFirst("api-error-response");
        if (errorNode != null) {
            wrapperNode = errorNode;
        }

        NodeWrapper subscriptionNode = wrapperNode.findFirst("subscription");
        if (subscriptionNode != null) {
            this.subscription = new Subscription(subscriptionNode);
        }

        NodeWrapper disbursementNode = wrapperNode.findFirst("disbursement");
        if (disbursementNode != null) {
            this.disbursement = new Disbursement(disbursementNode);
        }

        NodeWrapper merchantAccountNode = wrapperNode.findFirst("merchant-account");
        if (merchantAccountNode != null) {
            this.merchantAccount = new MerchantAccount(merchantAccountNode);
        }

        NodeWrapper disputeNode = wrapperNode.findFirst("dispute");
        if (disputeNode != null) {
            this.dispute = new Dispute(disputeNode);
        }

        NodeWrapper transactionNode = wrapperNode.findFirst("transaction");
        if (transactionNode != null) {
            this.transaction = new Transaction(transactionNode);
        }

        NodeWrapper transactionReviewNode = wrapperNode.findFirst("transaction-review");
        if (transactionReviewNode != null) {
            this.transactionReview = new TransactionReview(transactionReviewNode);
        }

        NodeWrapper partnerMerchantNode = wrapperNode.findFirst("partner-merchant");
        if (partnerMerchantNode != null) {
            this.partnerMerchant = new PartnerMerchant(partnerMerchantNode);
        }

        NodeWrapper oAuthAccessRevocationNode = wrapperNode.findFirst("oauth-application-revocation");
        if (oAuthAccessRevocationNode != null) {
            this.oauthAccessRevocation = new OAuthAccessRevocation(oAuthAccessRevocationNode);
        }

        NodeWrapper connectedMerchantStatusTransitionedNode = wrapperNode.findFirst("connected-merchant-status-transitioned");
        if (connectedMerchantStatusTransitionedNode != null) {
            this.connectedMerchantStatusTransitioned = new ConnectedMerchantStatusTransitioned(connectedMerchantStatusTransitionedNode);
        }

        NodeWrapper connectedMerchantPayPalStatusChangedNode = wrapperNode.findFirst("connected-merchant-paypal-status-changed");
        if (connectedMerchantPayPalStatusChangedNode != null) {
            this.connectedMerchantPayPalStatusChanged = new ConnectedMerchantPayPalStatusChanged(connectedMerchantPayPalStatusChangedNode);
        }

        NodeWrapper accountUpdaterDailyReportNode = wrapperNode.findFirst("account-updater-daily-report");
        if (accountUpdaterDailyReportNode != null) {
            this.accountUpdaterDailyReport = new AccountUpdaterDailyReport(accountUpdaterDailyReportNode);
        }

        NodeWrapper grantedPaymentInstrumentUpdateNode = wrapperNode.findFirst("granted-payment-instrument-update");
        if (grantedPaymentInstrumentUpdateNode != null) {
            this.grantedPaymentInstrumentUpdate = new GrantedPaymentInstrumentUpdate(grantedPaymentInstrumentUpdateNode);
        }

        if (kind == WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED || kind == WebhookNotification.Kind.PAYMENT_METHOD_REVOKED_BY_CUSTOMER) {
            this.revokedPaymentMethodMetadata = new RevokedPaymentMethodMetadata(wrapperNode);
        }

        NodeWrapper localPaymentReverseNode = wrapperNode.findFirst("local-payment-reversed");
        if (localPaymentReverseNode != null && kind == Kind.LOCAL_PAYMENT_REVERSED) {
            this.localPaymentReversed = new LocalPaymentReversed(localPaymentReverseNode);
        }

        NodeWrapper localPaymentNode = wrapperNode.findFirst("local-payment");
        if (localPaymentNode != null && kind == Kind.LOCAL_PAYMENT_COMPLETED) {
            this.localPaymentCompleted = new LocalPaymentCompleted(localPaymentNode);
        }

        NodeWrapper localPaymentExpiredNode = wrapperNode.findFirst("local-payment-expired");
        if (localPaymentExpiredNode != null && kind == Kind.LOCAL_PAYMENT_EXPIRED) {
            this.localPaymentExpired = new LocalPaymentExpired(localPaymentExpiredNode);
        }

        NodeWrapper localPaymentFundedNode = wrapperNode.findFirst("local-payment-funded");
        if (localPaymentFundedNode != null && kind == Kind.LOCAL_PAYMENT_FUNDED) {
            this.localPaymentFunded = new LocalPaymentFunded(localPaymentFundedNode);
        }

        NodeWrapper paymentMethodCustomerDataUpdatedMetadataNode = wrapperNode.findFirst("payment-method-customer-data-updated-metadata");
        if (paymentMethodCustomerDataUpdatedMetadataNode != null && kind == Kind.PAYMENT_METHOD_CUSTOMER_DATA_UPDATED) {
            this.paymentMethodCustomerDataUpdatedMetadata = new PaymentMethodCustomerDataUpdatedMetadata(paymentMethodCustomerDataUpdatedMetadataNode);
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

    public String getSourceMerchantId() {
        return this.sourceMerchantId;
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

    public TransactionReview getTransactionReview() {
        return this.transactionReview;
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

    public OAuthAccessRevocation getOAuthAccessRevocation() {
        return this.oauthAccessRevocation;
    }

    public ConnectedMerchantStatusTransitioned getConnectedMerchantStatusTransitioned() {
        return this.connectedMerchantStatusTransitioned;
    }

    public ConnectedMerchantPayPalStatusChanged getConnectedMerchantPayPalStatusChanged() {
        return this.connectedMerchantPayPalStatusChanged;
    }

    public AccountUpdaterDailyReport getAccountUpdaterDailyReport() {
        return this.accountUpdaterDailyReport;
    }

    public GrantedPaymentInstrumentUpdate getGrantedPaymentInstrumentUpdate() {
        return this.grantedPaymentInstrumentUpdate;
    }

    public RevokedPaymentMethodMetadata getRevokedPaymentMethodMetadata() {
        return this.revokedPaymentMethodMetadata;
    }

    public LocalPaymentCompleted getLocalPaymentCompleted() {
        return this.localPaymentCompleted;
    }

    public LocalPaymentExpired getLocalPaymentExpired() {
        return this.localPaymentExpired;
    }

    public LocalPaymentFunded getLocalPaymentFunded() {
        return this.localPaymentFunded;
    }

    public LocalPaymentReversed getLocalPaymentReversed() {
        return this.localPaymentReversed;
    }

    public PaymentMethodCustomerDataUpdatedMetadata getPaymentMethodCustomerDataUpdatedMetadata() {
        return this.paymentMethodCustomerDataUpdatedMetadata;
    }
}
