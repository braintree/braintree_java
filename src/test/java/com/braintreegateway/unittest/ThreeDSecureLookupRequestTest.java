package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.ThreeDSecureLookupRequest;
import com.braintreegateway.ThreeDSecureLookupAddress;
import com.braintreegateway.ThreeDSecureLookupAdditionalInformation;

import java.util.Map;
import com.fasterxml.jackson.jr.ob.JSON;

import org.junit.Test;
import static org.junit.Assert.*;

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

        assertEquals("10.00", request.getAmount());
        String outputJSON = request.toJSON();
        assertTrue(outputJSON.matches("^\\{.+\\}$"));
        assertTrue(outputJSON.matches("^.+\"amount\":\"" + request.getAmount() + "\".+$"));
        assertTrue(outputJSON.matches("^.+\"df_reference_id\":\"ABC-123\".+$"));
        assertTrue(outputJSON.matches("^.+\"authorizationFingerprint\":\"auth-fingerprint\".+$"));
        assertTrue(outputJSON.matches("^.+\"braintreeLibraryVersion\":\"braintree/web/3.44.0\".+$"));
        assertFalse(outputJSON.matches("^.+\"challengeRequested\":.+$"));
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

        ThreeDSecureLookupAddress billingAddress = new ThreeDSecureLookupAddress()
                .givenName("First")
                .surname("Last")
                .phoneNumber("1234567890")
                .locality("Oakland")
                .countryCodeAlpha2("US")
                .streetAddress("123 Address")
                .extendedAddress("Unit 2")
                .postalCode("94112")
                .region("CA");

        ThreeDSecureLookupAddress shippingAddress = new ThreeDSecureLookupAddress()
                .givenName("First")
                .surname("Last")
                .phoneNumber("0987654321")
                .locality("Beverly Hills")
                .countryCodeAlpha2("US")
                .streetAddress("123 Fake")
                .extendedAddress("Unit 3")
                .postalCode("90210")
                .region("CA");

        ThreeDSecureLookupAdditionalInformation additionalInfo = new ThreeDSecureLookupAdditionalInformation()
                .shippingAddress(shippingAddress)
                .productCode("1")
                .deliveryTimeframe("1")
                .deliveryEmail("last.first@example.com")
                .reorderIndicator("Y")
                .preorderIndicator("Y")
                .preorderDate("11/5/1955")
                .giftCardAmount("10.00")
                .giftCardCurrencyCode("USD")
                .giftCardCount("1")
                .accountAgeIndicator("1")
                .accountCreateDate("11/5/1955")
                .accountChangeIndicator("Y")
                .accountChangeDate("11/5/1955")
                .accountPwdChangeIndicator("Y")
                .accountPwdChangeDate("11/5/1955")
                .shippingAddressUsageIndicator("Y")
                .shippingAddressUsageDate("11/5/1955")
                .transactionCountDay("1")
                .transactionCountYear("1")
                .addCardAttempts("1")
                .accountPurchases("1")
                .fraudActivity("Y")
                .shippingNameIndicator("Y")
                .paymentAccountIndicator("Y")
                .paymentAccountAge("1")
                .addressMatch("Y")
                .accountId("1")
                .ipAddress("127.0.0.1")
                .orderDescription("Fake Description")
                .taxAmount("1")
                .userAgent("Interwebz")
                .authenticationIndicator("Y")
                .installment("1")
                .purchaseDate("11/5/1955")
                .recurringEnd("11/12/1955")
                .recurringFrequency("1");

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.additionalInformation(additionalInfo);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);

        String outputJSON = request.toJSON();
        assertTrue(outputJSON.matches("^\\{.+\\}$"));
        assertTrue(outputJSON.matches("^.+\"amount\":\"10.00\".+$"));
        assertTrue(outputJSON.matches("^.+\"email\":\"first.last@example.com\".+$"));

        assertTrue(outputJSON.matches("^.+\"billingGivenName\":\"First\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingSurname\":\"Last\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingLine1\":\"123 Address\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingLine2\":\"Unit 2\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingCity\":\"Oakland\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingState\":\"CA\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingPostalCode\":\"94112\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingCountryCode\":\"US\".+$"));
        assertTrue(outputJSON.matches("^.+\"billingPhoneNumber\":\"1234567890\".+$"));

        assertTrue(outputJSON.matches("^.+\"shipping_given_name\":\"First\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_surname\":\"Last\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_phone\":\"0987654321\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_line1\":\"123 Fake\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_line2\":\"Unit 3\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_city\":\"Beverly Hills\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_state\":\"CA\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_postal_code\":\"90210\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_country_code\":\"US\".+$"));

        assertTrue(outputJSON.matches("^.+\"product_code\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"delivery_timeframe\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"delivery_email\":\"last.first@example.com\".+$"));
        assertTrue(outputJSON.matches("^.+\"reorder_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"preorder_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"preorder_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"gift_card_amount\":\"10.00\".+$"));
        assertTrue(outputJSON.matches("^.+\"gift_card_currency_code\":\"USD\".+$"));
        assertTrue(outputJSON.matches("^.+\"gift_card_count\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_age_indicator\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_create_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_change_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_change_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_pwd_change_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_pwd_change_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_address_usage_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_address_usage_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"transaction_count_day\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"transaction_count_year\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"add_card_attempts\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_purchases\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"fraud_activity\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"shipping_name_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"payment_account_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"payment_account_age\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"address_match\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"account_id\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"ip_address\":\"127.0.0.1\".+$"));
        assertTrue(outputJSON.matches("^.+\"order_description\":\"Fake Description\".+$"));
        assertTrue(outputJSON.matches("^.+\"tax_amount\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"user_agent\":\"Interwebz\".+$"));
        assertTrue(outputJSON.matches("^.+\"authentication_indicator\":\"Y\".+$"));
        assertTrue(outputJSON.matches("^.+\"installment\":\"1\".+$"));
        assertTrue(outputJSON.matches("^.+\"purchase_date\":\"11/5/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"recurring_end\":\"11/12/1955\".+$"));
        assertTrue(outputJSON.matches("^.+\"recurring_frequency\":\"1\".+$"));
    }

    @Test
    public void serializesBillingAddressWithinAdditionalInformation() {
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

        ThreeDSecureLookupAddress billingAddress = new ThreeDSecureLookupAddress()
                .givenName("First")
                .surname("Last")
                .phoneNumber("1234567890")
                .locality("Oakland")
                .countryCodeAlpha2("US")
                .streetAddress("123 Address")
                .extendedAddress("Unit 2")
                .postalCode("94112")
                .region("CA");

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest()
            .amount("10.00")
            .clientData(clientData)
            .email("first.last@example.com")
            .billingAddress(billingAddress);

        String outputJSON = request.toJSON();

        try {
            Map<String, Object> JSONMap = JSON.std.mapFrom(outputJSON);
            Map<String, String> info = (Map) JSONMap.get("additional_info");

            assertEquals(info.get("billingGivenName"), "First");
            assertEquals(info.get("billingSurname"), "Last");
            assertEquals(info.get("billingLine1"), "123 Address");
            assertEquals(info.get("billingLine2"), "Unit 2");
            assertEquals(info.get("billingCity"), "Oakland");
            assertEquals(info.get("billingState"), "CA");
            assertEquals(info.get("billingPostalCode"), "94112");
            assertEquals(info.get("billingCountryCode"), "US");
            assertEquals(info.get("billingPhoneNumber"), "1234567890");
        } catch (IOException e) {
            fail(e.getMessage());
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

        String outputJSON = request.toJSON();
        assertTrue(outputJSON.matches("^\\{.+\\}$"));
        assertTrue(outputJSON.matches("^.+\"challengeRequested\":true.+$"));
    }

    @Test
    public void serializesWithDataOnlyRequestedTrue() {
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
        request.dataOnlyRequested(true);
        request.clientData(clientData);

        String outputJSON = request.toJSON();
        assertTrue(outputJSON.matches("^\\{.+\\}$"));
        assertTrue(outputJSON.matches("^.+\"dataOnlyRequested\":true.+$"));
    }
}
