package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.WebhookNotificationGateway;
import com.braintreegateway.WebhookTestingGateway;
import com.braintreegateway.util.SimpleNodeWrapper;

public class WebhookNotificationTest {
    private final Configuration configuration = new Configuration(
            Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

    // Builds a notification from validated fixture XML, bypassing signature checks.
    private WebhookNotification notificationFor(WebhookNotification.Kind kind, String id) {
        String payload = new WebhookTestingGateway(configuration)
                .sampleNotification(kind, id)
                .get("bt_payload");

        return new WebhookNotificationGateway(configuration).parseWithoutSignatureVerification(payload);
    }

    private WebhookNotification parseXml(String subjectXml, String kind) {
        StringBuilder builder = new StringBuilder();

        builder.append("<notification>");
        builder.append("<timestamp type=\"datetime\">2018-10-11T21:28:37Z</timestamp>");
        builder.append("<kind>").append(kind).append("</kind>");
        builder.append("<subject>").append(subjectXml).append("</subject>");
        builder.append("</notification>");

        return new WebhookNotification(SimpleNodeWrapper.parse(builder.toString()));
    }

    @Test
    public void parsesTopLevelFields() {
        WebhookNotification notification =
                notificationFor(WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id");

        assertEquals(WebhookNotification.Kind.TRANSACTION_DISBURSED, notification.getKind());
        assertNotNull(notification.getTimestamp());
        assertNull(notification.getErrors());
    }

    @Test
    public void exposesEachSubjectAccessor() {
        assertNotNull(notificationFor(
                WebhookNotification.Kind.SUBSCRIPTION_CHARGED_SUCCESSFULLY, "id").getSubscription());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.TRANSACTION_DISBURSED, "id").getTransaction());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.TRANSACTION_REVIEWED, "id").getTransactionReview());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.DISBURSEMENT, "id").getDisbursement());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.DISPUTE_OPENED, "id").getDispute());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.PARTNER_MERCHANT_CONNECTED, "id").getPartnerMerchant());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.OAUTH_ACCESS_REVOKED, "id").getOAuthAccessRevocation());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.CONNECTED_MERCHANT_STATUS_TRANSITIONED, "id")
                .getConnectedMerchantStatusTransitioned());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED, "id")
                .getConnectedMerchantPayPalStatusChanged());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.ACCOUNT_UPDATER_DAILY_REPORT, "id").getAccountUpdaterDailyReport());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD, "id")
                .getGrantedPaymentInstrumentUpdate());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED, "id").getRevokedPaymentMethodMetadata());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.LOCAL_PAYMENT_COMPLETED, "id").getLocalPaymentCompleted());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.LOCAL_PAYMENT_EXPIRED, "id").getLocalPaymentExpired());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.LOCAL_PAYMENT_FUNDED, "id").getLocalPaymentFunded());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.LOCAL_PAYMENT_REVERSED, "id").getLocalPaymentReversed());
        assertNotNull(notificationFor(
                WebhookNotification.Kind.PAYMENT_METHOD_CUSTOMER_DATA_UPDATED, "id")
                .getPaymentMethodCustomerDataUpdatedMetadata());
    }

    @Test
    public void parsesMerchantAccountFromSubject() {
        String subject =
                "<merchant-account>" +
                "<id>sub_merchant_account_id</id>" +
                "<status>active</status>" +
                "<currency-iso-code>USD</currency-iso-code>" +
                "</merchant-account>";

        WebhookNotification notification = parseXml(subject, "sub_merchant_account_approved");

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED, notification.getKind());
        assertNotNull(notification.getMerchantAccount());
        assertEquals("sub_merchant_account_id", notification.getMerchantAccount().getId());
        assertEquals(MerchantAccount.Status.ACTIVE, notification.getMerchantAccount().getStatus());
    }

    @Test
    public void parsesErrorsAndMerchantAccountFromApiErrorResponse() {
        String subject =
                "<api-error-response>" +
                "<message>Credit score is too low</message>" +
                "<errors>" +
                "<merchant-account>" +
                "<errors type=\"array\">" +
                "<error>" +
                "<code>82621</code>" +
                "<message>Credit score is too low</message>" +
                "<attribute type=\"symbol\">base</attribute>" +
                "</error>" +
                "</errors>" +
                "</merchant-account>" +
                "</errors>" +
                "<merchant-account>" +
                "<id>sub_merchant_account_id</id>" +
                "<status>suspended</status>" +
                "</merchant-account>" +
                "</api-error-response>";

        WebhookNotification notification = parseXml(subject, "sub_merchant_account_declined");

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, notification.getKind());
        assertNotNull(notification.getMerchantAccount());
        assertEquals("sub_merchant_account_id", notification.getMerchantAccount().getId());

        assertNotNull(notification.getErrors());
        assertEquals(
                "Credit score is too low",
                notification.getErrors().forObject("merchant-account").onField("base").get(0).getMessage());
    }
}
