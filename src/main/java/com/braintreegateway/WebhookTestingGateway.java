package com.braintreegateway;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Sha1Hasher;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class WebhookTestingGateway {
    private Configuration configuration;

    public WebhookTestingGateway(Configuration configuration) {
        this.configuration = configuration;
    }

    private String buildPayload(WebhookNotification.Kind kind, String id, String sourceMerchantId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = dateFormat.format(new Date());

        String payload = "<notification>";
        payload += "<timestamp type=\"datetime\">" + timestamp + "</timestamp>";
        payload += "<kind>" + kind + "</kind>";
        if (sourceMerchantId != null) {
            payload += "<source-merchant-id>" + sourceMerchantId + "</source-merchant-id>";
        }
        payload += "<subject>" + subjectXml(kind, id) + "</subject>";
        payload += "</notification>";

        return Base64.encodeBase64String(payload.getBytes()).replace("\r", "");
    }

    private String publicKeySignaturePair(String stringToSign) {
        return String.format("%s|%s", configuration.getPublicKey(), new Sha1Hasher().hmacHash(configuration.getPrivateKey(), stringToSign));
    }

    public HashMap<String, String> sampleNotification(WebhookNotification.Kind kind, String id) {
        return sampleNotification(kind, id, null);
    }

    public HashMap<String, String> sampleNotification(WebhookNotification.Kind kind, String id, String sourceMerchantId) {
        HashMap<String, String> response = new HashMap<String, String>();
        String payload = buildPayload(kind, id, sourceMerchantId);
        response.put("bt_payload", payload);
        response.put("bt_signature", publicKeySignaturePair(payload));

        return response;
    }

    private String subjectXml(WebhookNotification.Kind kind, String id) {
        switch (kind) {
            case CHECK: return checkXml();
            case ACCOUNT_UPDATER_DAILY_REPORT: return accountUpdaterDailyReportXml(id);
            case CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED: return connectedMerchantPayPalStatusChangedXml(id);
            case CONNECTED_MERCHANT_STATUS_TRANSITIONED: return connectedMerchantStatusTransitionedXml(id);
            case DISBURSEMENT: return disbursementXml(id);
            case DISPUTE_ACCEPTED: return disputeAcceptedXml(id);
            case DISPUTE_AUTO_ACCEPTED: return disputeAutoAcceptedXml(id);
            case DISPUTE_DISPUTED: return disputeDisputedXml(id);
            case DISPUTE_EXPIRED: return disputeExpiredXml(id);
            case DISPUTE_LOST: return disputeLostXml(id);
            case DISPUTE_OPENED: return disputeOpenedXml(id);
            case DISPUTE_UNDER_REVIEW: return disputeUnderReviewXml(id);
            case DISPUTE_WON: return disputeWonXml(id);
            case GRANTED_PAYMENT_METHOD_REVOKED: return grantedPaymentMethodRevokedXml(id);
            case GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD: return grantedPaymentInstrumentUpdateXml();
            case LOCAL_PAYMENT_COMPLETED: return localPaymentCompletedXml(id);
            case LOCAL_PAYMENT_EXPIRED: return localPaymentExpiredXml();
            case LOCAL_PAYMENT_FUNDED: return localPaymentFundedXml();
            case LOCAL_PAYMENT_REVERSED: return localPaymentReversedXml();
            case OAUTH_ACCESS_REVOKED: return oauthAccessRevokedXml(id);
            case PARTNER_MERCHANT_CONNECTED: return partnerMerchantConnectedXml(id);
            case PARTNER_MERCHANT_DECLINED: return partnerMerchantDeclinedXml(id);
            case PARTNER_MERCHANT_DISCONNECTED: return partnerMerchantDisconnectedXml(id);
            case PAYMENT_METHOD_CUSTOMER_DATA_UPDATED: return paymentMethodCustomerDataUpdatedXml(id);
            case PAYMENT_METHOD_REVOKED_BY_CUSTOMER: return paymentMethodRevokedByCustomerXml(id);
            case RECIPIENT_UPDATED_GRANTED_PAYMENT_METHOD: return grantedPaymentInstrumentUpdateXml();
            case REFUND_FAILED: return refundFailedXml(id);
            case SUBSCRIPTION_BILLING_SKIPPED: return subscriptionBillingSkippedXml(id);
            case SUBSCRIPTION_CHARGED_SUCCESSFULLY: return subscriptionChargedSuccessfullyXml(id);
            case SUBSCRIPTION_CHARGED_UNSUCCESSFULLY: return subscriptionChargedUnsuccessfullyXml(id);
            case TRANSACTION_DISBURSED: return transactionXml(id);
            case TRANSACTION_RETRIED: return transactionRetriedXml(id);
            case TRANSACTION_REVIEWED: return transactionReviewXml(id);
            case TRANSACTION_SETTLED: return transactionSettledXml(id);
            case TRANSACTION_SETTLEMENT_DECLINED: return transactionSettlementDeclinedXml(id);
            default: return subscriptionXml(id);
        }
    }

    private String[][] TYPE_DATE = {{"type", "date"}};
    private String[][] TYPE_DATE_TIME = {{"type", "datetime"}};
    private String[][] TYPE_ARRAY = {{"type", "array"}};
    private String[][] TYPE_SYMBOL = {{"type", "symbol"}};
    private String[][] TYPE_BOOLEAN = {{"type", "boolean"}};
    private String[][] NIL_TRUE = {{"nil", "true"}};

    private String subscriptionXml(String id) {
        return node("subscription",
                node("id", id),
                node("transactions", TYPE_ARRAY),
                node("add_ons", TYPE_ARRAY),
                node("discounts", TYPE_ARRAY)
        );
    }

    private String subscriptionBillingSkippedXml(String id) {
        return node("subscription",
                node("id", id),
                node("transactions", TYPE_ARRAY),
                node("add_ons", TYPE_ARRAY),
                node("discounts", TYPE_ARRAY)
        );
    }

    private String subscriptionChargedSuccessfullyXml(String id) {
        return node("subscription",
                node("id", id),
                node("add_ons", TYPE_ARRAY),
                node("transactions",
                    node("transaction",
                        node("id", "1"),
                        node("status", "submitted_for_settlement"),
                        node("amount", "49.99"),
                        node("billing"),
                        node("credit-card"),
                        node("customer"),
                        node("descriptor"),
                        node("shipping"),
                        node("disbursement-details", TYPE_ARRAY),
                        node("subscription")
                    )
                ),
                node("discounts", TYPE_ARRAY)
        );
    }

    private String subscriptionChargedUnsuccessfullyXml(String id) {
        return node("subscription",
                node("id", id),
                node("add_ons", TYPE_ARRAY),
                node("transactions",
                    node("transaction",
                        node("id", "1"),
                        node("status", "failed"),
                        node("amount", "49.99"),
                        node("billing"),
                        node("credit-card"),
                        node("customer"),
                        node("descriptor"),
                        node("shipping"),
                        node("disbursement-details", TYPE_ARRAY),
                        node("subscription")
                    )
                ),
                node("discounts", TYPE_ARRAY)
        );
    }

    private String transactionXml(String id) {
        return node("transaction",
                node("id", id),
                node("amount", "100"),
                node("disbursement-details",
                    node("disbursement-date", TYPE_DATE, "2013-07-09")
                ),
                node("billing"),
                node("credit-card"),
                node("customer"),
                node("descriptor"),
                node("shipping"),
                node("subscription")
        );
    }

    private String transactionRetriedXml(String id) {
        return node("transaction",
                node("id", id),
                node("amount", "100"),
                node("status", "submitted_for_settlement"),
                node("type", "sale"),
                node("retried-transaction-id", "original_txn_id")
        );
    }

    private String transactionReviewXml(String id) {
        return node("transaction-review",
                node("transaction-id", "my-id"),
                node("decision", "a smart decision"),
                node("reviewer-email", "hey@girl.com"),
                node("reviewer-note", "i reviewed this"),
                node("reviewed-time", TYPE_DATE_TIME, "2018-10-11T21:28:37Z")
        );
    }

    private String transactionSettledXml(String id) {
        return node("transaction",
                node("id", id),
                node("status", "settled"),
                node("amount", "100"),
                node("us-bank-account",
                    node("routing-number", "123456789"),
                    node("last-4", "1234"),
                    node("account-type", "checking"),
                    node("account-holder-name", "Dan Schulman"),
                    node("ach-mandate",
                        node("text", "Sample ACH Mandate Text"),
                        node("accepted-at", "2017-01-17")
                    )
                ),
                node("disbursement-details"),
                node("billing"),
                node("credit-card"),
                node("customer"),
                node("descriptor"),
                node("shipping"),
                node("subscription")
        );
    }

    private String transactionSettlementDeclinedXml(String id) {
        return node("transaction",
                node("id", id),
                node("status", "settlement_declined"),
                node("amount", "100"),
                node("us-bank-account",
                    node("routing-number", "123456789"),
                    node("last-4", "1234"),
                    node("account-type", "checking"),
                    node("account-holder-name", "Dan Schulman"),
                    node("ach-mandate",
                        node("text", "Sample ACH Mandate Text"),
                        node("accepted-at", "2017-01-17")
                    )
                ),
                node("disbursement-details"),
                node("billing"),
                node("credit-card"),
                node("customer"),
                node("descriptor"),
                node("shipping"),
                node("subscription")
        );
    }

    private String disputeOpenedXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "open"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeUnderReviewXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "under_review"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeLostXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "lost"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeWonXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("date-won", TYPE_DATE, "2014-03-22"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "won"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeAcceptedXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "accepted"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeAutoAcceptedXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "0.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "auto_accepted"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeDisputedXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "disputed"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disputeExpiredXml(String id) {
        return node("dispute",
                node("id", id),
                node("amount", "250.00"),
                node("amount-disputed", "250.00"),
                node("amount-won", "245.00"),
                node("received-date", TYPE_DATE, "2014-03-21"),
                node("reply-by-date", TYPE_DATE, "2014-03-21"),
                node("date-opened", TYPE_DATE, "2014-03-21"),
                node("kind", "chargeback"),
                node("currency-iso-code", "USD"),
                node("status", "expired"),
                node("reason", "fraud"),
                node("transaction",
                    node("id", id),
                    node("amount", "250.00")
                )
        );
    }

    private String disbursementXml(String id) {
        return node("disbursement",
                node("id", id),
                node("transaction-ids", TYPE_ARRAY,
                    node("item", "asdf"),
                    node("item", "qwer")
                ),
                node("success", TYPE_BOOLEAN, "true"),
                node("retry", TYPE_BOOLEAN, "false"),
                node("exception-message", NIL_TRUE),
                node("amount", "100.00"),
                node("disbursement-date", TYPE_DATE, "2014-02-10"),
                node("follow-up-action", NIL_TRUE),
                node("merchant-account",
                    node("id", "merchant_account_token"),
                    node("currency-iso-code", "USD"),
                    node("status", "active")
                )
        );
    }

    private String partnerMerchantConnectedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123"),
                node("merchant-public-id", "public_id"),
                node("public-key", "public_key"),
                node("private-key", "private_key"),
                node("client-side-encryption-key", "cse_key")
        );
    }

    private String partnerMerchantDisconnectedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123")
        );
    }

    private String partnerMerchantDeclinedXml(String id) {
        return node("partner-merchant",
                node("partner-merchant-id", "abc123")
        );
    }

    private String refundFailedXml(String id) {
        return node("transaction",
                node("id", id),
                node("amount", "100"),
                node("credit-card"),
                node("status", "processor_decined"),
                node("refunded-transaction-fk")
        );
    }

    private String oauthAccessRevokedXml(String id) {
        return node("oauth-application-revocation",
                node("merchant-id", id),
                node("oauth-application-client-id", "oauth_application_client_id")
        );
    }

    private String connectedMerchantStatusTransitionedXml(String id) {
        return node("connected-merchant-status-transitioned",
                node("oauth-application-client-id", "oauth_application_client_id"),
                node("merchant-public-id", id),
                node("status", "new_status")
        );
    }

    private String connectedMerchantPayPalStatusChangedXml(String id) {
        return node("connected-merchant-paypal-status-changed",
                node("oauth-application-client-id", "oauth_application_client_id"),
                node("merchant-public-id", id),
                node("action", "link")
        );
    }

    private String accountUpdaterDailyReportXml(String id) {
        return node("account-updater-daily-report",
                node("report-url", "link-to-csv-report"),
                node("report-date", TYPE_DATE, "2016-01-14")
        );
    }

    private String grantedPaymentInstrumentUpdateXml() {
        return node("granted-payment-instrument-update",
                node("grant-owner-merchant-id", "vczo7jqrpwrsi2px"),
                node("grant-recipient-merchant-id", "cf0i8wgarszuy6hc"),
                node("payment-method-nonce",
                    node("nonce", "ee257d98-de40-47e8-96b3-a6954ea7a9a4"),
                    node("consumed", TYPE_BOOLEAN, "false"),
                    node("locked", TYPE_BOOLEAN, "false")
                    ),
                node("token", "abc123z"),
                node("updated-fields", TYPE_ARRAY,
                    node("item", "expiration-month"),
                    node("item", "expiration-year")
                    )
                );
    }

    private String grantedPaymentMethodRevokedXml(String id) {
        return venmoAccountXml(id);
    }

    private String paymentMethodRevokedByCustomerXml(String id) {
        return node("paypal-account",
                node("billing-agreement-id", "a-billing-agreement-id"),
                node("created-at", TYPE_DATE_TIME, "2019-01-01T12:00:00Z"),
                node("customer-id", "a-customer-id"),
                node("default", TYPE_BOOLEAN, "true"),
                node("email", "name@email.com"),
                node("global-id", "cGF5bWVudG1ldGhvZF9jaDZieXNz"),
                node("image-url", "https://assets.braintreegateway.com/payment_method_logo/paypal.png?environment=test"),
                node("token", id),
                node("updated-at", TYPE_DATE_TIME, "2019-01-02T12:00:00Z"),
                node("is-channel-initiated", NIL_TRUE, ""),
                node("payer-id", "a-payer-id"),
                node("payer-info", NIL_TRUE, ""),
                node("limited-use-order-id", NIL_TRUE, ""),
                node("revoked-at", TYPE_DATE_TIME, "2019-01-02T12:00:00Z")
        );
    }

    private String localPaymentCompletedXml(String id) {
        if(id == "blik_one_click_id") {
            return blikOneClickLocalPaymentCompletedXml();
        } else {
            return defaultLocalPaymentCompletedXml();
        }
    }

    private String defaultLocalPaymentCompletedXml() {
        return node("local-payment",
                node("bic", "a-bic"),
                node("iban-last-chars", "1234"),
                node("payer-id", "a-payer-id"),
                node("payer-name", "a-payer-name"),
                node("payment-id", "a-payment-id"),
                node("payment-method-nonce", "ee257d98-de40-47e8-96b3-a6954ea7a9a4"),
                node("transaction",
                     node("id", "1"),
                     node("status", "authorizing"),
                     node("amount", "10.00"),
                     node("order-id", "order1234")
                    )
                );
    }

    private String blikOneClickLocalPaymentCompletedXml() {
        return node("local-payment",
                node("bic", "a-bic"),
                node("iban-last-chars", "1234"),
                node("blik-aliases", TYPE_ARRAY,
                    node("blik-alias",
                         node("key", "unique-key-1"),
                         node("label", "unique-label-1")
                        )
                    ),
                node("payer-id", "a-payer-id"),
                node("payer-name", "a-payer-name"),
                node("payment-id", "a-payment-id"),
                node("payment-method-nonce", "ee257d98-de40-47e8-96b3-a6954ea7a9a4"),
                node("transaction",
                     node("id", "1"),
                     node("status", "authorizing"),
                     node("amount", "10.00"),
                     node("order-id", "order1234")
                    )
                );
    }

    private String localPaymentFundedXml() {
        return node("local-payment-funded",
                node("payment-id", "a-payment-id"),
                node("payment-context-id", "a-payment-context-id"),
                node("transaction",
                     node("id", "1"),
                     node("status", "settled"),
                     node("amount", "10.00"),
                     node("order-id", "order1234")
                    )
        );
    }

    private String localPaymentExpiredXml() {
        return node("local-payment-expired",
                node("payment-id", "a-payment-id"),
                node("payment-context-id", "a-payment-context-id")
        );
    }

    private String localPaymentReversedXml() {
        return node("local-payment-reversed",
                node("payment-id", "a-payment-id")
        );
    }

    private String paymentMethodCustomerDataUpdatedXml(String id) {
        return node("payment-method-customer-data-updated-metadata",
                node("token", "TOKEN-12345"),
                node("payment-method", venmoAccountXml(id)),
                node("datetime-updated", "2022-01-01T21:28:37Z"),
                node("enriched-customer-data",
                     node("fields-updated", TYPE_ARRAY,
                          node("item", "username")
                     ),
                     node("profile-data",
                          node("username", "venmo_username"),
                          node("first-name", "John"),
                          node("last-name", "Doe"),
                          node("phone-number", "1231231234"),
                          node("email", "john.doe@paypal.com"),
                          node("billing-address",
                              node("street-address", "billing-street-addr"),
                              node("extended-address", "billing-extended-addr"),
                              node("locality", "billing-locality"),
                              node("region", "billing-region"),
                              node("postal-code", "billing-code")
                          ),
                          node("shipping-address",
                              node("street-address", "shipping-street-addr"),
                              node("extended-address", "shipping-extended-addr"),
                              node("locality", "shipping-locality"),
                              node("region", "shipping-region"),
                              node("postal-code", "shipping-code")
                          )
                     )
                )
        );
    }

    private String venmoAccountXml(String id) {
        return node("venmo-account",
                node("created-at", TYPE_DATE_TIME, "2018-10-11T21:28:37Z"),
                node("updated-at", TYPE_DATE_TIME, "2018-10-11T21:28:37Z"),
                node("default", TYPE_BOOLEAN, "true"),
                node("image-url", "https://assets.braintreegateway.com/payment_method_logo/venmo.png?environment=test"),
                node("token", id),
                node("source-description", "Venmo Account: venmojoe"),
                node("username", "venmojoe"),
                node("venmo-user-id", "456"),
                node("customer-id", "venmo_customer_id"),
                node("global-id", "cGF5bWVudG1ldGhvZF92ZW5tb2FjY291bnQ")
        );
    }

    private String checkXml() {
        return node("check", TYPE_BOOLEAN, "true");
    }

    private static String node(String name, String... contents) {
        return node(name, null, contents);
    }

    private static String node(String name, String[][] attributes, String... contents) {
        StringBuilder buffer = new StringBuilder();
        buffer.append('<').append(name);
        if (attributes != null) {
            for (String[] pair : attributes) {
                buffer.append(" ");
                buffer.append(pair[0]).append("=\"").append(pair[1]).append("\"");
            }
        }
        buffer.append('>');
        for (String content : contents) {
            buffer.append(content);
        }
        buffer.append("</").append(name).append('>');
        return buffer.toString();
    }
}
