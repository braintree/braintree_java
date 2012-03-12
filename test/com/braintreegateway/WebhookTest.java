package com.braintreegateway;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.InvalidSignatureException;

public class WebhookTest {
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @Test
    public void verifyCreatesAVerificationString() {
        String verification = this.gateway.webhook().verify("verification_token");
        Assert.assertEquals("integration_public_key|c9f15b74b0d98635cd182c51e2703cffa83388c3", verification);
    }

    @Test
    public void createsSampleNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_PAST_DUE, "my_id");

        WebhookNotification notification = this.gateway.webhook().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        Assert.assertEquals(WebhookNotification.Kind.SUBSCRIPTION_PAST_DUE, notification.getKind());
        Assert.assertEquals("my_id", notification.getSubscription().getId());
        Assert.assertTrue(notification.getTimestamp() instanceof Calendar);
    }

    @Test(expected = InvalidSignatureException.class)
    public void invalidSignatureRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_PAST_DUE, "my_id");

        this.gateway.webhook().parse(sampleNotification.get("signature") + "bad_stuff", sampleNotification.get("payload"));
    }

    @Test(expected = InvalidSignatureException.class)
    public void signatureWithoutMatchingPublicKeyRaisesException() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_PAST_DUE, "my_id");

        this.gateway.webhook().parse("uknown_public_key|signature", sampleNotification.get("payload"));
    }
}