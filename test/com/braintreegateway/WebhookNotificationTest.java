package com.braintreegateway;

import java.util.Calendar;
import java.util.HashMap;

import com.braintreegateway.testhelpers.TestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.exceptions.InvalidSignatureException;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class WebhookNotificationTest {
    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

	@Test
	public void createNotificationWithUnrecognizedKind() {
		String xml = "<notification><kind>" + "bad_kind" + "</kind></notification>";
		NodeWrapper node = NodeWrapperFactory.instance.create(xml);
		
		WebhookNotification notification = new WebhookNotification(node);
		Assert.assertEquals(WebhookNotification.Kind.UNRECOGNIZED, notification.getKind());
	}

    @Test
    public void verifyCreatesAVerificationString() {
        String verification = this.gateway.webhookNotification().verify("verification_token");
        Assert.assertEquals("integration_public_key|c9f15b74b0d98635cd182c51e2703cffa83388c3", verification);
    }

    @Test
    public void createsSampleNotification() {
        HashMap<String, String> sampleNotification = this.gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, "my_id");

        WebhookNotification notification = this.gateway.webhookNotification().parse(sampleNotification.get("signature"), sampleNotification.get("payload"));

        Assert.assertEquals(WebhookNotification.Kind.SUBSCRIPTION_WENT_PAST_DUE, notification.getKind());
        Assert.assertEquals("my_id", notification.getSubscription().getId());
        TestHelper.assertDatesEqual(Calendar.getInstance(), notification.getTimestamp());
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
}
