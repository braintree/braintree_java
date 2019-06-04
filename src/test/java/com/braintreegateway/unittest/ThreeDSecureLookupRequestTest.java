package com.braintreegateway.unittest;

import com.braintreegateway.ThreeDSecureLookupRequest;
import com.braintreegateway.ThreeDSecureLookupAddress;
import com.braintreegateway.ThreeDSecureLookupAdditionalInformation;
import com.braintreegateway.exceptions.BraintreeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ThreeDSecureLookupRequestTest {

    @Test
    public void serializesWithAmountAndClientData() {
        String clientData = "{\n" +
                "  \"authorizationFingerprint\": \"auth-fingerprint\",\n" +
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
            System.out.print(outputJSON);
            assertTrue(outputJSON.matches("^\\{.+\\}$"));
            assertTrue(outputJSON.matches("^.+\"amount\":\"" + request.getAmount() + "\".+$"));
            assertTrue(outputJSON.matches("^.+\"df_reference_id\":\"ABC-123\".+$"));
            assertTrue(outputJSON.matches("^.+\"authorizationFingerprint\":\"auth-fingerprint\".+$"));
            assertTrue(outputJSON.matches("^.+\"braintreeLibraryVersion\":\"braintree/web/3.44.0\".+$"));
            assertFalse(outputJSON.matches("^.+\"challengeRequested\":.+$"));
        } catch (BraintreeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void serializesAdditionalInformation() {
        String clientData = "{\n" +
                "  \"authorizationFingerprint\": \"auth-fingerprint\",\n" +
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

        ThreeDSecureLookupAddress billingAddress = new ThreeDSecureLookupAddress().
            givenName("First").
            surname("Last").
            phoneNumber("1234567890").
            locality("Oakland").
            countryCodeAlpha2("US").
            streetAddress("123 Address").
            extendedAddress("Unit 2").
            postalCode("94112").
            region("CA");

        ThreeDSecureLookupAdditionalInformation additionalInfo = new ThreeDSecureLookupAdditionalInformation().
            email("first.last@example.com").
            billingAddress(billingAddress);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.additionalInformation(additionalInfo);

        try {
            String outputJSON = request.toJSON();
            assertTrue(outputJSON.matches("^.+\"billingGivenName\":\"First\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingSurname\":\"Last\".+$"));
            assertTrue(outputJSON.matches("^.+\"email\":\"first.last@example.com\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingLine1\":\"123 Address\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingLine2\":\"Unit 2\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingCity\":\"Oakland\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingState\":\"CA\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingPostalCode\":\"94112\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingCountryCode\":\"US\".+$"));
            assertTrue(outputJSON.matches("^.+\"billingPhoneNumber\":\"1234567890\".+$"));
        } catch (BraintreeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void serializesWithChallengeRequestedTrue() {
        String clientData = "{\n" +
                "  \"authorizationFingerprint\": \"auth-fingerprint\",\n" +
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
            String outputJSON = request.toJSON();
            assertTrue(outputJSON.matches("^.+\"challengeRequested\":true.+$"));
        } catch (BraintreeException e) {
            System.out.println(e.getMessage());
        }
    }
}
