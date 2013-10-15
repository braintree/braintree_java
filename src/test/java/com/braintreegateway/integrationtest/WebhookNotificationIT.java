package com.braintreegateway.integrationtest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.ValidationErrorCode;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

public class WebhookNotificationIT {
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void createNotificationWithUnrecognizedKind() {
        String xml = "<notification><kind>" + "bad_kind" + "</kind> <subject> </subject> </notification>";
        NodeWrapper node = NodeWrapperFactory.instance.create(xml);

        WebhookNotification notification = new WebhookNotification(node);
        assertEquals(WebhookNotification.Kind.UNRECOGNIZED, notification.getKind());
    }

    @Test
    public void verifyCreatesAVerificationString() {
        String verification = this.gateway.webhookNotification().verify("verification_token");
        assertEquals("integration_public_key|c9f15b74b0d98635cd182c51e2703cffa83388c3", verification);
    }

    @Test
    public void createsSampleSubscriptionNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, notification.getKind());
        assertEquals("my_id", notification.getSubscription().getId());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleMerchantAccountApprovedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        assertEquals(MerchantAccount.Status.ACTIVE, notification.getMerchantAccount().getStatus());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleMerchantAccountDeclinedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        assertEquals(MerchantAccount.Status.SUSPENDED, notification.getMerchantAccount().getStatus());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleMerchantAccountDeclinedNotificationWithErrorCodes() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_APPLICANT_DETAILS_DECLINED_OFAC, notification.getErrors().forObject("merchantAccount").onField("base").get(0).getCode());
    }

    @Test(expected = InvalidSignatureException.class)
    public void invalidSignatureRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        this.gateway.webhookNotification().parse(sampleNotification.get("signature") + "bad_stuff", sampleNotification.get("payload"));
    }

    @Test(expected = InvalidSignatureException.class)
    public void signatureWithoutMatchingPublicKeyRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        this.gateway.webhookNotification().parse("uknown_public_key|signature", sampleNotification.get("payload"));
    }

    @Test
    public void createsSampleTransactionDisbursedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_DISBURSED, notification.getKind());
        assertEquals("my_id", notification.getTransaction().getId());
        assertEquals(2013, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.YEAR));
        assertEquals(Calendar.JULY, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.MONTH));
        assertEquals(9, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void buildsSampleNotificationForPartnerMerchantConnectedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.PARTNER_MERCHANT_CONNECTED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.PARTNER_MERCHANT_CONNECTED, notification.getKind());
        assertEquals("public_id", notification.getPartnerMerchant().getMerchantPublicId());
        assertEquals("public_key", notification.getPartnerMerchant().getPublicKey());
        assertEquals("private_key", notification.getPartnerMerchant().getPrivateKey());
        assertEquals("abc123", notification.getPartnerMerchant().getPartnerMerchantId());
        assertEquals("cse_key", notification.getPartnerMerchant().getClientSideEncryptionKey());
        long now = new Date().getTime();
        long age = now - notification.getTimestamp().getTime().getTime();
        assertTrue(age < 5000);
    }

    @Test
    public void buildsSampleNotificationForPartnerMerchantDeclinedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.PARTNER_MERCHANT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.PARTNER_MERCHANT_DECLINED, notification.getKind());
        assertEquals("abc123", notification.getPartnerMerchant().getPartnerMerchantId());
        long now = new Date().getTime();
        long age = now - notification.getTimestamp().getTime().getTime();
        assertTrue(age < 5000);
    }

    @Test
    public void buildsSampleNotificationForPartnerMerchantDisconnectedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.PARTNER_MERCHANT_DISCONNECTED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        assertEquals(WebhookNotification.Kind.PARTNER_MERCHANT_DISCONNECTED, notification.getKind());
        assertEquals("abc123", notification.getPartnerMerchant().getPartnerMerchantId());
        assertEquals(null, notification.getPartnerMerchant().getMerchantPublicId());
        assertEquals(null, notification.getPartnerMerchant().getPublicKey());
        assertEquals(null, notification.getPartnerMerchant().getPrivateKey());
        long now = new Date().getTime();
        long age = now - notification.getTimestamp().getTime().getTime();
        assertTrue(age < 5000);
    }
}
