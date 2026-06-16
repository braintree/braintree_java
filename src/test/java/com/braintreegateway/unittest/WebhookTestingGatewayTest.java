package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.WebhookNotificationGateway;
import com.braintreegateway.WebhookTestingGateway;

public class WebhookTestingGatewayTest {
    private final Configuration configuration = new Configuration(
            Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

    private WebhookNotification parse(HashMap<String, String> sample) {
        return new WebhookNotificationGateway(configuration)
                .parse(sample.get("bt_signature"), sample.get("bt_payload"));
    }

    @Test
    public void sampleNotificationBuildsParsablePayloadForEveryKind() {
        WebhookTestingGateway gateway = new WebhookTestingGateway(configuration);

        for (WebhookNotification.Kind kind : WebhookNotification.Kind.values()) {
            HashMap<String, String> sample = gateway.sampleNotification(kind, "my_id");

            assertNotNull(sample.get("bt_payload"), "payload for " + kind);
            assertNotNull(sample.get("bt_signature"), "signature for " + kind);

            WebhookNotification notification = parse(sample);

            assertEquals(kind, notification.getKind(), "kind round-trips for " + kind);
            assertNotNull(notification.getTimestamp(), "timestamp for " + kind);
        }
    }

    @Test
    public void sampleNotificationBuildsBlikOneClickLocalPaymentCompletedPayload() {
        WebhookTestingGateway gateway = new WebhookTestingGateway(configuration);

        HashMap<String, String> sample =
                gateway.sampleNotification(WebhookNotification.Kind.LOCAL_PAYMENT_COMPLETED, "blik_one_click_id");

        WebhookNotification notification = parse(sample);

        assertEquals(WebhookNotification.Kind.LOCAL_PAYMENT_COMPLETED, notification.getKind());
        assertEquals(1, notification.getLocalPaymentCompleted().getBlikAlias().size());
    }

    @Test
    public void sampleNotificationIncludesSourceMerchantIdWhenProvided() {
        WebhookTestingGateway gateway = new WebhookTestingGateway(configuration);

        HashMap<String, String> sample = gateway.sampleNotification(
                WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id", "source_merchant_id");

        WebhookNotification notification = parse(sample);

        assertEquals("source_merchant_id", notification.getSourceMerchantId());
    }

    @Test
    public void sampleNotificationOmitsSourceMerchantIdWhenNull() {
        WebhookTestingGateway gateway = new WebhookTestingGateway(configuration);

        HashMap<String, String> sample =
                gateway.sampleNotification(WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id");

        WebhookNotification notification = parse(sample);

        assertNull(notification.getSourceMerchantId());
    }
}
