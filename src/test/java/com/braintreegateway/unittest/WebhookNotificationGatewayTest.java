package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.WebhookNotificationGateway;
import com.braintreegateway.WebhookTestingGateway;
import com.braintreegateway.exceptions.InvalidChallengeException;
import com.braintreegateway.exceptions.InvalidSignatureException;

public class WebhookNotificationGatewayTest {
    private final Configuration configuration = new Configuration(
            Environment.DEVELOPMENT, "merchant_id", "integration_public_key", "integration_private_key");

    private WebhookNotificationGateway gateway() {
        return new WebhookNotificationGateway(configuration);
    }

    private HashMap<String, String> sample() {
        return new WebhookTestingGateway(configuration)
                .sampleNotification(WebhookNotification.Kind.TRANSACTION_DISBURSED, "my_id");
    }

    @Test
    public void parseValidatesSignatureAndReturnsNotification() {
        HashMap<String, String> sample = sample();

        WebhookNotification notification =
                gateway().parse(sample.get("bt_signature"), sample.get("bt_payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_DISBURSED, notification.getKind());
    }

    @Test
    public void parseThrowsWhenSignatureIsNull() {
        Exception e = assertThrows(InvalidSignatureException.class, () -> {
            gateway().parse(null, sample().get("bt_payload"));
        });
        assertEquals("signature cannot be null", e.getMessage());
    }

    @Test
    public void parseThrowsWhenPayloadIsNull() {
        Exception e = assertThrows(InvalidSignatureException.class, () -> {
            gateway().parse(sample().get("bt_signature"), null);
        });
        assertEquals("payload cannot be null", e.getMessage());
    }

    @Test
    public void parseThrowsWhenPayloadContainsIllegalCharacters() {
        Exception e = assertThrows(InvalidSignatureException.class, () -> {
            gateway().parse(sample().get("bt_signature"), "bad payload ^&*");
        });
        assertEquals("payload contains illegal characters", e.getMessage());
    }

    @Test
    public void parseThrowsWhenNoMatchingPublicKey() {
        Exception e = assertThrows(InvalidSignatureException.class, () -> {
            gateway().parse("unknown_public_key|asignature", sample().get("bt_payload"));
        });
        assertEquals("no matching public key", e.getMessage());
    }

    @Test
    public void parseThrowsWhenSignatureDoesNotMatchPayload() {
        Exception e = assertThrows(InvalidSignatureException.class, () -> {
            gateway().parse("integration_public_key|deadbeef", sample().get("bt_payload"));
        });
        assertEquals("signature does not match payload - one has been modified", e.getMessage());
    }

    @Test
    public void parseWithoutSignatureVerificationSkipsSignatureCheck() {
        WebhookNotification notification =
                gateway().parseWithoutSignatureVerification(sample().get("bt_payload"));

        assertEquals(WebhookNotification.Kind.TRANSACTION_DISBURSED, notification.getKind());
    }

    @Test
    public void verifyReturnsPublicKeySignaturePairForHexChallenge() {
        String challenge = "20a4bcdaed0db4cd0c1234ab";

        String response = gateway().verify(challenge);

        assertTrue(response.startsWith("integration_public_key|"));
        assertEquals(2, response.split("\\|").length);
    }

    @Test
    public void verifyThrowsForNonHexChallenge() {
        Exception e = assertThrows(InvalidChallengeException.class, () -> {
            gateway().verify("bad challenge!");
        });
        assertEquals("challenge contains non-hex characters", e.getMessage());
    }
}
