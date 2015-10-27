package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.MerchantAccount;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.exceptions.InvalidChallengeException;
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
        String verification = this.gateway.webhookNotification().verify("20f9f8ed05f77439fe955c977e4c8a53");
        assertEquals("integration_public_key|d9b899556c966b3f06945ec21311865d35df3ce4", verification);
    }

    @Test
    public void invalidChallengeRaisesException() {
        try {
          this.gateway.webhookNotification().verify("bad challenge");
          fail("Should have throw exception, but did not.");
        }
        catch(final InvalidChallengeException e)
        {
          final String expectedMessage = "challenge contains non-hex characters";
          assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void createsSampleSubscriptionNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, notification.getKind());
        assertEquals("my_id", notification.getSubscription().getId());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleSubscriptionChargedSuccessfullyNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_SUCCESSFULLY, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_SUCCESSFULLY, notification.getKind());
        assertEquals("my_id", notification.getSubscription().getId());
        assertEquals(1, notification.getSubscription().getTransactions().size());

        Transaction transaction = notification.getSubscription().getTransactions().get(0);
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
        assertEquals("49.99", transaction.getAmount().toString());
    }

    @Test
    public void createsSampleMerchantAccountApprovedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_APPROVED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        assertEquals(MerchantAccount.Status.ACTIVE, notification.getMerchantAccount().getStatus());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleMerchantAccountDeclinedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        assertEquals(MerchantAccount.Status.SUSPENDED, notification.getMerchantAccount().getStatus());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
    }

    @Test
    public void createsSampleMerchantAccountDeclinedNotificationWithErrorCodes() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUB_MERCHANT_ACCOUNT_DECLINED, notification.getKind());
        assertEquals("my_id", notification.getMerchantAccount().getId());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
        assertEquals(ValidationErrorCode.MERCHANT_ACCOUNT_DECLINED_OFAC, notification.getErrors().forObject("merchantAccount").onField("base").get(0).getCode());
    }

    @Test
    public void createsSampleDisputeOpenedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISPUTE_OPENED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISPUTE_OPENED, notification.getKind());
        assertEquals("my_id", notification.getDispute().getId());
        assertEquals(Dispute.Status.OPEN, notification.getDispute().getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, notification.getDispute().getKind());
        assertNotNull(notification.getDispute().getOpenedDate());
    }

    public void createsSampleDisputeWonNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISPUTE_WON, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISPUTE_WON, notification.getKind());
        assertEquals("my_id", notification.getDispute().getId());
        assertEquals(Dispute.Status.WON, notification.getDispute().getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, notification.getDispute().getKind());
        assertNotNull(notification.getDispute().getOpenedDate());
    }

    public void createsSampleDisputeLostNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISPUTE_LOST, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISPUTE_LOST, notification.getKind());
        assertEquals("my_id", notification.getDispute().getId());
        assertEquals(Dispute.Status.LOST, notification.getDispute().getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, notification.getDispute().getKind());
        assertNotNull(notification.getDispute().getOpenedDate());
        assertNotNull(notification.getDispute().getWonDate());
    }

    @Test(expected = InvalidSignatureException.class)
    public void invalidSignatureRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature") + "bad_stuff", sampleNotification.get("bt_payload"));
    }

    @Test
    public void signatureWithoutMatchingPublicKeyRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        try {
          this.gateway.webhookNotification().parse("unknown_public_key|signature", sampleNotification.get("bt_payload"));
          fail("Should have throw exception, but did not.");
        }
        catch(final InvalidSignatureException e)
        {
          final String expectedMessage = "no matching public key";
          assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void retriesPayloadWithNewLine() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        try {
          this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload").trim());
        }
        catch(final InvalidSignatureException e)
        {
          fail("Threw an exception, but it should not have: " + e.getMessage());
        }
    }

    @Test
    public void signatureWithChangedPayloadRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        try {
          this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), "badstuff" + sampleNotification.get("bt_payload"));
          fail("Should have throw exception, but did not.");
        }
        catch(final InvalidSignatureException e)
        {
          final String expectedMessage = "signature does not match payload - one has been modified";
          assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void payloadWithInvalidCharactersRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        try {
          this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), "~*~* Invalid! *~*~");
          fail("Should have throw exception, but did not.");
        }
        catch(final InvalidSignatureException e)
        {
          final String expectedMessage = "payload contains illegal characters";
          assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void parseAllowsAllValidCharacters() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        try {
          this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+=/\n");
          fail("Should have throw exception, but did not.");
        }
        catch(final InvalidSignatureException e)
        {
          final String unexpectedMessage = "payload contains illegal characters";
          assertFalse(unexpectedMessage.equals(e.getMessage()));
        }
    }

    @Test
    public void createsSampleTransactionDisbursedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_DISBURSED, notification.getKind());
        assertEquals("my_id", notification.getTransaction().getId());
        assertEquals(2013, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.YEAR));
        assertEquals(Calendar.JULY, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.MONTH));
        assertEquals(9, notification.getTransaction().getDisbursementDetails().getDisbursementDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void createsSampleDisbursementNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISBURSEMENT, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISBURSEMENT, notification.getKind());
        assertEquals("my_id", notification.getDisbursement().getId());
        assertEquals(null, notification.getDisbursement().getExceptionMessage());
        assertEquals(2014, notification.getDisbursement().getDisbursementDate().get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, notification.getDisbursement().getDisbursementDate().get(Calendar.MONTH));
        assertEquals(10, notification.getDisbursement().getDisbursementDate().get(Calendar.DAY_OF_MONTH));
        assertEquals(null, notification.getDisbursement().getFollowUpAction());
    }

    @Test
    public void createsSampleDisbursementExceptionNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISBURSEMENT_EXCEPTION, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISBURSEMENT_EXCEPTION, notification.getKind());
        assertEquals("my_id", notification.getDisbursement().getId());
        assertEquals("bank_rejected", notification.getDisbursement().getExceptionMessage());
        assertEquals(2014, notification.getDisbursement().getDisbursementDate().get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, notification.getDisbursement().getDisbursementDate().get(Calendar.MONTH));
        assertEquals(10, notification.getDisbursement().getDisbursementDate().get(Calendar.DAY_OF_MONTH));
        assertEquals("update_account_information", notification.getDisbursement().getFollowUpAction());
    }

    @Test
    public void buildsSampleNotificationForPartnerMerchantConnectedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.PARTNER_MERCHANT_CONNECTED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

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
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

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
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.PARTNER_MERCHANT_DISCONNECTED, notification.getKind());
        assertEquals("abc123", notification.getPartnerMerchant().getPartnerMerchantId());
        assertEquals(null, notification.getPartnerMerchant().getMerchantPublicId());
        assertEquals(null, notification.getPartnerMerchant().getPublicKey());
        assertEquals(null, notification.getPartnerMerchant().getPrivateKey());
        long now = new Date().getTime();
        long age = now - notification.getTimestamp().getTime().getTime();
        assertTrue(age < 5000);
    }

    @Test
    public void buildsSampleNotificationForCheck()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.CHECK, "");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.CHECK, notification.getKind());
        long now = new Date().getTime();
        long age = now - notification.getTimestamp().getTime().getTime();
        assertTrue(age < 5000);
    }
}
