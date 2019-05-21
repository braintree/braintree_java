package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureLookup;
import com.braintreegateway.ThreeDSecureLookupRequest;
import com.braintreegateway.exceptions.BraintreeException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ThreeDSecureLookupTest {

    @Test
    public void setsPropertiesFromHash() {
        Map<String, String> lookupResponse = new HashMap<String, String>();
        lookupResponse.put("acsUrl", "https://braintreepayments.com");
        lookupResponse.put("threeDSecureVersion", "2.0");
        lookupResponse.put("transactionId", "123-txn-id");
        lookupResponse.put("unused", "value");

        ThreeDSecureLookup lookup = new ThreeDSecureLookup(lookupResponse);

        assertEquals("https://braintreepayments.com", lookup.getAcsUrl());
        assertEquals("2.0", lookup.getThreeDSecureVersion());
        assertEquals("123-txn-id", lookup.getTransactionId());
    }

    @Test
    public void doesntThrowExceptionWhenDataChecksOut() {
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
        request.amount("10.00");
        request.clientData(clientData);

        try {
            assertEquals("10.00", request.getAmount());
            String outputJSON = request.toJSON();
            assertTrue(outputJSON.matches("^\\{.+\\}$"));
            assertTrue(outputJSON.matches("^.+\"amount\":\"" + request.getAmount() + "\".+$"));
            assertFalse(outputJSON.matches("^.+\"challengeRequested\":.+$"));
        } catch (BraintreeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void setsChallengeRequestedToTrue_inJson() {
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
        request.amount("10.00");
        request.challengeRequested(true);
        request.clientData(clientData);

        try {
            assertEquals("10.00", request.getAmount());
            String outputJSON = request.toJSON();
            assertTrue(outputJSON.matches("^.+\"challengeRequested\":true.+$"));
        } catch (BraintreeException e) {
            System.out.println(e.getMessage());
        }
    }
}
