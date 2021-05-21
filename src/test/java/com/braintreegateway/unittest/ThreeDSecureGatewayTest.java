package com.braintreegateway.unittest;

import java.util.regex.Pattern;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.BraintreeException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreeDSecureGatewayTest {

    @Test
    public void lookupThrowsExceptionWhenAmountIsMissing() {
        BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");

        String clientData = "{\n" +
                "  \"authorizationFingerprint\": \"bad-auth-fingerprint\",\n" +
                "  \"braintreeLibraryVersion\": \"braintree/web/3.44.0\",\n" +
                "  \"dfReferenceId\": \"ABC-123\",\n" +
                "  \"nonce\": \"FAKE-NONCE\",\n" +
                "  \"clientMetadata\": {\n" +
                "    \"cardinalDeviceDataCollectionTimeElapsed\": 40,\n" +
                "    \"issuerDeviceDataCollectionResult\": true,\n" +
                "    \"issuerDeviceDataCollectionTimeElapsed\": 413,\n" +
                "    \"requestedThreeDSecureVersion\": \"2\",\n" +
                "    \"sdkVersion\": \"web/3.42.0\"\n" +
                "  }\n" +
                "}";

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.clientData(clientData);

        Exception e = assertThrows(BraintreeException.class, () -> {
            gateway.threeDSecure().lookup(request);
        });

        assertTrue(Pattern.matches("Amount required", e.getMessage()));
    }

    @Test
    public void lookupThrowsExceptionWhenNonceIsMissing() {
        BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");

        String clientData = "{\n" +
                "  \"authorizationFingerprint\": \"bad-auth-fingerprint\",\n" +
                "  \"braintreeLibraryVersion\": \"braintree/web/3.44.0\",\n" +
                "  \"dfReferenceId\": \"ABC-123\",\n" +
                "  \"clientMetadata\": {\n" +
                "    \"cardinalDeviceDataCollectionTimeElapsed\": 40,\n" +
                "    \"issuerDeviceDataCollectionResult\": true,\n" +
                "    \"issuerDeviceDataCollectionTimeElapsed\": 413,\n" +
                "    \"requestedThreeDSecureVersion\": \"2\",\n" +
                "    \"sdkVersion\": \"web/3.42.0\"\n" +
                "  }\n" +
                "}";

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);

        Exception e = assertThrows(BraintreeException.class, () -> {
            gateway.threeDSecure().lookup(request);
        });

        assertTrue(Pattern.matches("Payment method nonce required", e.getMessage()));
    }
}
