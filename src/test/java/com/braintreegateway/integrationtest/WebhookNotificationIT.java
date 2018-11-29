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
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

public class WebhookNotificationIT extends IntegrationTest {

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
    public void createsSampleSubscriptionChargedUnsuccessfullyNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_UNSUCCESSFULLY, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_UNSUCCESSFULLY, notification.getKind());
        assertEquals("my_id", notification.getSubscription().getId());
        assertEquals(1, notification.getSubscription().getTransactions().size());

        Transaction transaction = notification.getSubscription().getTransactions().get(0);
        assertEquals(Transaction.Status.FAILED, transaction.getStatus());
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

    @Test
    public void createsSampleDisputeWonNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISPUTE_WON, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISPUTE_WON, notification.getKind());
        assertEquals("my_id", notification.getDispute().getId());
        assertEquals(Dispute.Status.WON, notification.getDispute().getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, notification.getDispute().getKind());
        assertNotNull(notification.getDispute().getOpenedDate());
    }

    @Test
    public void createsSampleDisputeLostNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.DISPUTE_LOST, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.DISPUTE_LOST, notification.getKind());
        assertEquals("my_id", notification.getDispute().getId());
        assertEquals(Dispute.Status.LOST, notification.getDispute().getStatus());
        assertEquals(Dispute.Kind.CHARGEBACK, notification.getDispute().getKind());
        assertNotNull(notification.getDispute().getOpenedDate());
        assertNull(notification.getDispute().getWonDate());
    }

    @Test
    public void createsSampleNotificationWithSourceMerchantId() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id", "my_source_merchant_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals("my_source_merchant_id", notification.getSourceMerchantId());
    }

    @Test
    public void createsSampleNotificationWithoutSourceMerchantIdIfUnspecified() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertNull(notification.getSourceMerchantId());
    }

    @Test(expected = InvalidSignatureException.class)
    public void invalidSignatureRaisesExceptionWhenSignatureIsNull() {
        this.gateway.webhookNotification().parse(null, "payload");
    }

    @Test(expected = InvalidSignatureException.class)
    public void invalidSignatureRaisesExceptionWhenPayloadIsNull() {
        this.gateway.webhookNotification().parse("signature", null);
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
    public void createsSampleTransactionSettledNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.TRANSACTION_SETTLED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_SETTLED, notification.getKind());

        Transaction transaction = notification.getTransaction();

        assertEquals("100", transaction.getAmount().toString());
        assertEquals(Transaction.Status.SETTLED, transaction.getStatus());

        UsBankAccountDetails usBankAccountDetails = transaction.getUsBankAccountDetails();
        assertEquals("123456789", usBankAccountDetails.getRoutingNumber());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
    }

    @Test
    public void createsSampleTransactionSettlementDeclinedNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.TRANSACTION_SETTLEMENT_DECLINED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_SETTLEMENT_DECLINED, notification.getKind());

        Transaction transaction = notification.getTransaction();

        assertEquals("100", transaction.getAmount().toString());
        assertEquals(Transaction.Status.SETTLEMENT_DECLINED, transaction.getStatus());

        UsBankAccountDetails usBankAccountDetails = transaction.getUsBankAccountDetails();
        assertEquals("123456789", usBankAccountDetails.getRoutingNumber());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
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
    public void buildsSampleNotificationForOAuthAccessRevocation()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.OAUTH_ACCESS_REVOKED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.OAUTH_ACCESS_REVOKED, notification.getKind());
        assertEquals("my_id", notification.getOAuthAccessRevocation().getMerchantId());
        assertEquals("oauth_application_client_id", notification.getOAuthAccessRevocation().getOauthApplicationClientId());
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
    public void buildsSampleNotificationForConnectedMerchantStatusTransitionedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.CONNECTED_MERCHANT_STATUS_TRANSITIONED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.CONNECTED_MERCHANT_STATUS_TRANSITIONED, notification.getKind());
        assertEquals("my_id", notification.getConnectedMerchantStatusTransitioned().getMerchantPublicId());
        assertEquals("my_id", notification.getConnectedMerchantStatusTransitioned().getMerchantId());
        assertEquals("new_status", notification.getConnectedMerchantStatusTransitioned().getStatus());
        assertEquals("oauth_application_client_id", notification.getConnectedMerchantStatusTransitioned().getOAuthApplicationClientId());
    }

    @Test
    public void buildsSampleNotificationForConnectedMerchantPayPalStatusChangedWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.CONNECTED_MERCHANT_PAYPAL_STATUS_CHANGED, notification.getKind());
        assertEquals("my_id", notification.getConnectedMerchantPayPalStatusChanged().getMerchantPublicId());
        assertEquals("my_id", notification.getConnectedMerchantPayPalStatusChanged().getMerchantId());
        assertEquals("link", notification.getConnectedMerchantPayPalStatusChanged().getAction());
        assertEquals("oauth_application_client_id", notification.getConnectedMerchantPayPalStatusChanged().getOAuthApplicationClientId());
    }

    @Test
    public void createsSampleNotificationForIdealPaymentComplete() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.IDEAL_PAYMENT_COMPLETE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.IDEAL_PAYMENT_COMPLETE, notification.getKind());

        IdealPayment idealPayment = notification.getIdealPayment();
        assertEquals("my_id", idealPayment.getId());
        assertEquals("COMPLETE", idealPayment.getStatus());
        assertEquals("ORDERABC", idealPayment.getOrderId());
        assertEquals("10.00", idealPayment.getAmount().toString());
        assertEquals("https://example.com", idealPayment.getApprovalUrl());
        assertEquals("1234567890", idealPayment.getIdealTransactionId());
    }

    @Test
    public void createsSampleNotificationForIdealPaymentFailed() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.IDEAL_PAYMENT_FAILED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.IDEAL_PAYMENT_FAILED, notification.getKind());

        IdealPayment idealPayment = notification.getIdealPayment();
        assertEquals("my_id", idealPayment.getId());
        assertEquals("FAILED", idealPayment.getStatus());
        assertEquals("ORDERABC", idealPayment.getOrderId());
        assertEquals("10.00", idealPayment.getAmount().toString());
        assertEquals("https://example.com", idealPayment.getApprovalUrl());
        assertEquals("1234567890", idealPayment.getIdealTransactionId());
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

    @Test
    public void buildsSampleNotificationForAccountUpdaterDailyReportWebhook()
    {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting()
            .sampleNotification(WebhookNotification.Kind.ACCOUNT_UPDATER_DAILY_REPORT, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification()
            .parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.ACCOUNT_UPDATER_DAILY_REPORT, notification.getKind());
        assertEquals("link-to-csv-report", notification.getAccountUpdaterDailyReport().getReportUrl());

        assertEquals(2016, notification.getAccountUpdaterDailyReport().getReportDate().get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, notification.getAccountUpdaterDailyReport().getReportDate().get(Calendar.MONTH));
        assertEquals(14, notification.getAccountUpdaterDailyReport().getReportDate().get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void createsSampleNotificationForGrantedPaymentInstrumentUpdate() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.GRANTED_PAYMENT_INSTRUMENT_UPDATE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.GRANTED_PAYMENT_INSTRUMENT_UPDATE, notification.getKind());

        GrantedPaymentInstrumentUpdate update = notification.getGrantedPaymentInstrumentUpdate();

        assertEquals("vczo7jqrpwrsi2px", update.getGrantOwnerMerchantId());
        assertEquals("cf0i8wgarszuy6hc", update.getGrantRecipientMerchantId());
        assertEquals("ee257d98-de40-47e8-96b3-a6954ea7a9a4", update.getPaymentMethodNonce());
        assertEquals("abc123z", update.getToken());
        assertEquals("expiration-month", update.getUpdatedFields().get(0));
        assertEquals("expiration-year", update.getUpdatedFields().get(1));
        assertEquals(2, update.getUpdatedFields().size());
    }

    @Test
    public void createsSampleNotificationForGrantedCreditCardRevoked() {
        String webhookXmlResponse = "<notification>"
            + "<source-merchant-id>12345</source-merchant-id>"
            + "<timestamp type='datetime'>2018-10-10T22:46:41Z</timestamp>"
            + "<kind>granted_payment_method_revoked</kind>"
            + "<subject>"
            + "<credit-card>"
            + "<bin>555555</bin>"
            + "<card-type>MasterCard</card-type>"
            + "<cardholder-name>Amber Ankunding</cardholder-name>"
            + "<commercial>Unknown</commercial>"
            + "<country-of-issuance>Unknown</country-of-issuance>"
            + "<created-at type='datetime'>2018-10-10T22:46:41Z</created-at>"
            + "<customer-id>credit_card_customer_id</customer-id>"
            + "<customer-location>US</customer-location>"
            + "<debit>Unknown</debit>"
            + "<default type='boolean'>true</default>"
            + "<durbin-regulated>Unknown</durbin-regulated>"
            + "<expiration-month>06</expiration-month>"
            + "<expiration-year>2020</expiration-year>"
            + "<expired type='boolean'>false</expired>"
            + "<global-id>cGF5bWVudG1ldGhvZF8zcHQ2d2hz</global-id>"
            + "<healthcare>Unknown</healthcare>"
            + "<image-url>https://assets.braintreegateway.com/payment_method_logo/mastercard.png?environment=test</image-url>"
            + "<issuing-bank>Unknown</issuing-bank>"
            + "<last-4>4444</last-4>"
            + "<payroll>Unknown</payroll>"
            + "<prepaid>Unknown</prepaid>"
            + "<product-id>Unknown</product-id>"
            + "<subscriptions type='array'/>"
            + "<token>credit_card_token</token>"
            + "<unique-number-identifier>08199d188e37460163207f714faf074a</unique-number-identifier>"
            + "<updated-at type='datetime'>2018-10-10T22:46:41Z</updated-at>"
            + "<venmo-sdk type='boolean'>false</venmo-sdk>"
            + "<verifications type='array'/>"
            + "</credit-card>"
            + "</subject>"
            + "</notification>";
        NodeWrapper webhookResponseNode = NodeWrapperFactory.instance.create(webhookXmlResponse);
        WebhookNotification notification = new WebhookNotification(webhookResponseNode);
        assertEquals(WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED, notification.getKind());

        RevokedPaymentMethodMetadata metadata = notification.getRevokedPaymentMethodMetadata();

        assertEquals("credit_card_token", metadata.getToken());
        assertEquals("credit_card_customer_id", metadata.getCustomerId());
        assertTrue(metadata.getRevokedPaymentMethod() instanceof CreditCard);
    }

    @Test
    public void createsSampleNotificationForGrantedPayPalAccountRevoked() {
        String webhookXmlResponse = "<notification>"
            + "<source-merchant-id>12345</source-merchant-id>"
            + "<timestamp type='datetime'>2018-10-10T22:46:41Z</timestamp>"
            + "<kind>granted_payment_method_revoked</kind>"
            + "<subject>"
            + "<paypal-account>"
            + "<billing-agreement-id>billing_agreement_id</billing-agreement-id>"
            + "<created-at type='dateTime'>2018-10-11T21:10:33Z</created-at>"
            + "<customer-id>paypal_customer_id</customer-id>"
            + "<default type='boolean'>true</default>"
            + "<email>johndoe@example.com</email>"
            + "<global-id>cGF5bWVudG1ldGhvZF9wYXlwYWxfdG9rZW4</global-id>"
            + "<image-url>https://jsdk.bt.local:9000/payment_method_logo/paypal.png?environment=test</image-url>"
            + "<subscriptions type='array'></subscriptions>"
            + "<token>paypal_token</token>"
            + "<updated-at type='dateTime'>2018-10-11T21:10:33Z</updated-at>"
            + "<payer-id>a6a8e1a4</payer-id>"
            + "</paypal-account>"
            + "</subject>"
            + "</notification>";
        NodeWrapper webhookResponseNode = NodeWrapperFactory.instance.create(webhookXmlResponse);
        WebhookNotification notification = new WebhookNotification(webhookResponseNode);
        assertEquals(WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED, notification.getKind());

        RevokedPaymentMethodMetadata metadata = notification.getRevokedPaymentMethodMetadata();

        assertEquals("paypal_token", metadata.getToken());
        assertEquals("paypal_customer_id", metadata.getCustomerId());
        assertTrue(metadata.getRevokedPaymentMethod() instanceof PayPalAccount);
    }

    @Test
    public void createsSampleNotificationForGrantedVenmoAccountRevoked() {
        String webhookXmlResponse = "<notification>"
            + "<source-merchant-id>12345</source-merchant-id>"
            + "<timestamp type='datetime'>2018-10-10T22:46:41Z</timestamp>"
            + "<kind>granted_payment_method_revoked</kind>"
            + "<subject>"
            + "<venmo-account>"
            + "<created-at type='dateTime'>2018-10-11T21:28:37Z</created-at>"
            + "<updated-at type='dateTime'>2018-10-11T21:28:37Z</updated-at>"
            + "<default type='boolean'>true</default>"
            + "<image-url>https://jsdk.bt.local:9000/payment_method_logo/venmo.png?environment=test</image-url>"
            + "<token>venmo_token</token>"
            + "<source-description>Venmo Account: venmojoe</source-description>"
            + "<username>venmojoe</username>"
            + "<venmo-user-id>456</venmo-user-id>"
            + "<subscriptions type='array'/>"
            + "<customer-id>venmo_customer_id</customer-id>"
            + "<global-id>cGF5bWVudG1ldGhvZF92ZW5tb2FjY291bnQ</global-id>"
            + "</venmo-account>"
            + "</subject>"
            + "</notification>";
        NodeWrapper webhookResponseNode = NodeWrapperFactory.instance.create(webhookXmlResponse);
        WebhookNotification notification = new WebhookNotification(webhookResponseNode);
        assertEquals(WebhookNotification.Kind.GRANTED_PAYMENT_METHOD_REVOKED, notification.getKind());

        RevokedPaymentMethodMetadata metadata = notification.getRevokedPaymentMethodMetadata();

        assertEquals("venmo_token", metadata.getToken());
        assertEquals("venmo_customer_id", metadata.getCustomerId());
        assertTrue(metadata.getRevokedPaymentMethod() instanceof VenmoAccount);
    }

    @Test
    public void createsLocalPaymentCompleted() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.LOCAL_PAYMENT_COMPLETED, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("bt_signature"), sampleNotification.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.LOCAL_PAYMENT_COMPLETED, notification.getKind());

        LocalPaymentCompleted payment = notification.getLocalPaymentCompleted();

        assertEquals("a-payment-id", payment.getPaymentId());
        assertEquals("a-payer-id", payment.getPayerId());
    }
}
