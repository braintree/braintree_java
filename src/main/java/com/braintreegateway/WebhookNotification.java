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
        OAUTH_ACCESS_REVOKED("oauth_access_revoked"),
        CONNECTED_MERCHANT_STATUS_TRANSITIONED("connected_merchant_status_transitioned"),
        CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED("connected_merchant_paypal_status_changed"),
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
        TRANSACTION_SETTLED("transaction_settled"),
        TRANSACTION_SETTLEMENT_DECLINED("transaction_settlement_declined"),
        DISBURSEMENT_EXCEPTION("disbursement_exception"),
        DISBURSEMENT("disbursement"),
        DISPUTE_OPENED("dispute_opened"),
        DISPUTE_LOST("dispute_lost"),
        DISPUTE_WON("dispute_won"),
        ACCOUNT_UPDATER_DAILY_REPORT("account_updater_daily_report"),
        IDEAL_PAYMENT_COMPLETE("ideal_payment_complete"),
        IDEAL_PAYMENT_FAILED("ideal_payment_failed"),
        // NEXT_MAJOR_VERSION remove GRANTED_PAYMENT_INSTRUMENT_UPDATE. Kind is not sent by Braintree Gateway.
        // Kind will either be GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD or RECIPIENT_UPDATED_GRANTED_PAYMENT_METHOD.
        GRANTED_PAYMENT_INSTRUMENT_UPDATE("granted_payment_instrument_update"),
        GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD("grantor_updated_granted_payment_method"),
        RECIPIENT_UPDATED_GRANTED_PAYMENT_METHOD("recipient_updated_granted_payment_method"),
        GRANTED_PAYMENT_METHOD_REVOKED("granted_payment_method_revoked"),
        LOCAL_PAYMENT_COMPLETED("local_payment_completed"),
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
    private OAuthAccessRevocation oauthAccessRevocation;
    private AccountUpdaterDailyReport accountUpdaterDailyReport;
    private ConnectedMerchantStatusTransitioned connectedMerchantStatusTransitioned;
    private ConnectedMerchantPayPalStatusChanged connectedMerchantPayPalStatusChanged;
    private IdealPayment idealPayment;
    private GrantedPaymentInstrumentUpdate grantedPaymentInstrumentUpdate;
    private RevokedPaymentMethodMetadata revokedPaymentMethodMetadata;
    private LocalPaymentCompleted localPaymentCompleted;
    private String sourceMerchantId;

    public WebhookNotification(NodeWrapper node) {
        this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"), Kind.UNRECOGNIZED);
        this.timestamp = node.findDateTime("timestamp");

        this.sourceMerchantId = node.findString("source-merchant-id");

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

        if (wrapperNode.findFirst("oauth-application-revocation") != null) {
            this.oauthAccessRevocation = new OAuthAccessRevocation(wrapperNode.findFirst("oauth-application-revocation"));
        }

        if (wrapperNode.findFirst("connected-merchant-status-transitioned") != null) {
            this.connectedMerchantStatusTransitioned = new ConnectedMerchantStatusTransitioned(wrapperNode.findFirst("connected-merchant-status-transitioned"));
        }

        if (wrapperNode.findFirst("connected-merchant-paypal-status-changed") != null) {
            this.connectedMerchantPayPalStatusChanged = new ConnectedMerchantPayPalStatusChanged(wrapperNode.findFirst("connected-merchant-paypal-status-changed"));
        }

        if (wrapperNode.findFirst("account-updater-daily-report") != null) {
            this.accountUpdaterDailyReport = new AccountUpdaterDailyReport(wrapperNode.findFirst("account-updater-daily-report"));
        }

        if (wrapperNode.findFirst("ideal-payment") != null) {
            this.idealPayment = new IdealPayment(wrapperNode.findFirst("ideal-payment"));
        }

        if (wrapperNode.findFirst("granted-payment-instrument-update") != null) {
            this.grantedPaymentInstrumentUpdate = new GrantedPaymentInstrumentUpdate(wrapperNode.findFirst("granted-payment-instrument-update"));
        }

        if (kind == WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED) {
            this.revokedPaymentMethodMetadata = new RevokedPaymentMethodMetadata(wrapperNode);
        }

        if (wrapperNode.findFirst("local-payment") != null) {
            this.localPaymentCompleted = new LocalPaymentCompleted(wrapperNode.findFirst("local-payment"));
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

    public IdealPayment getIdealPayment() {
        return this.idealPayment;
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
}
