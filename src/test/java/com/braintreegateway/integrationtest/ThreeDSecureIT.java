package com.braintreegateway.integrationtest;

import java.util.regex.Pattern;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;

public class ThreeDSecureIT extends IntegrationTest implements MerchantAccountTestConstants {

    @Test
    public void lookupThreeDSecure() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

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

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);

        if (outerResult.getErrors() != null) {
            StringBuilder results = new StringBuilder();
            results.append("validation errors:\n");
            for (ValidationError error : outerResult.getErrors().getAllDeepValidationErrors()) {
                results.append(error.getCode());
                results.append("\n");
                results.append(error.getMessage());
                results.append("\n");
            }
            fail(results.toString());
        }

        ThreeDSecureLookupResponse result = outerResult.getTarget();
        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());
        assertNotNull(paymentMethod.getThreeDSecureInfo());
        assertTrue(paymentMethod.getThreeDSecureInfo().isLiabilityShiftPossible());
        assertFalse(paymentMethod.getThreeDSecureInfo().isLiabilityShifted());
        assertNotNull(lookup.getAcsUrl());
        assertNotNull(lookup.getThreeDSecureVersion());
        assertNotNull(lookup.getTransactionId());
    }

    @Test
    public void lookupThreeDSecure_versionTwoTestCard() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("5200000000001005").
            expirationMonth("01").
            expirationYear("2022");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

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

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);

        setDeviceDataFields(request);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request).getTarget();

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

        assertNotNull(paymentMethod.getThreeDSecureInfo());
        assertNotNull(lookup.getThreeDSecureVersion());
        assertNull(paymentMethod.getThreeDSecureInfo().getAcsTransactionId());
        assertNull(paymentMethod.getThreeDSecureInfo().getParesStatus());
        assertNull(paymentMethod.getThreeDSecureInfo().getThreeDSecureServerTransactionId());
        assertNull(paymentMethod.getThreeDSecureInfo().getThreeDSecureLookupInfo().getTransStatus());
        assertNull(paymentMethod.getThreeDSecureInfo().getThreeDSecureLookupInfo().getTransStatusReason());
    }

    @Test
    public void lookupThreeDSecure_partialCustomerInfo() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);
        request.email("first.last@example.com");

        setDeviceDataFields(request);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request).getTarget();

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());
        assertNotNull(paymentMethod.getThreeDSecureInfo());
        assertTrue(paymentMethod.getThreeDSecureInfo().isLiabilityShiftPossible());
        assertFalse(paymentMethod.getThreeDSecureInfo().isLiabilityShifted());
        assertNotNull(lookup.getAcsUrl());
        assertNotNull(lookup.getThreeDSecureVersion());
        assertNotNull(lookup.getTransactionId());
    }

    @Test
    public void lookupThreeDSecure_missingCustomerInfo() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);

        setDeviceDataFields(request);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request).getTarget();

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());
        assertNotNull(paymentMethod.getThreeDSecureInfo());
        assertTrue(paymentMethod.getThreeDSecureInfo().isLiabilityShiftPossible());
        assertFalse(paymentMethod.getThreeDSecureInfo().isLiabilityShifted());
        assertNotNull(lookup.getAcsUrl());
        assertNotNull(lookup.getThreeDSecureVersion());
        assertNotNull(lookup.getTransactionId());
    }

    @Test
    public void lookupThreeDSecure_throwsBraintreeException_forInvalidAuthorizationFingerprint() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);

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

        String clientData = getClientDataString(nonce, "bad-auth-fingerprint");

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);

        try {
            gateway.threeDSecure().lookup(request);
            fail("Should throw BraintreeException");
        } catch (BraintreeException e) {
            assertTrue(Pattern.matches(".*Authorization fingerprint is invalid.*", e.getMessage()));
        }
    }

    @Test
    public void lookupThreeDSecure_specifyMerchantAccountSucceedsIfTransactionMerchantAccountMatches() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001000").
            expirationMonth("12").
            expirationYear("2022");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

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

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount(TransactionAmount.AUTHORIZE.amount.toString());
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);
        request.merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request).getTarget();

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());

        TransactionRequest transactionRequest = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            paymentMethodNonce(paymentMethod.getNonce());
        Result<Transaction> transactionResult = gateway.transaction().sale(transactionRequest);
        assert(transactionResult.isSuccess());
    }

    @Test
    public void lookupThreeDSecure_specifyMerchantAccountFailsIfTransactionMerchantAccountDoesNotMatch() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001000").
            expirationMonth("12").
            expirationYear("2022");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

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

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount(TransactionAmount.AUTHORIZE.amount.toString());
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.billingAddress(billingAddress);
        request.merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request).getTarget();

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());

        TransactionRequest transactionRequest = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(DEFAULT_MERCHANT_ACCOUNT_ID).
            paymentMethodNonce(paymentMethod.getNonce());
        Result<Transaction> transactionResult = gateway.transaction().sale(transactionRequest);
        assertFalse(transactionResult.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_MERCHANT_ACCOUNT_DOES_NOT_MATCH3_D_SECURE_MERCHANT_ACCOUNT,
            transactionResult.getErrors().forObject("transaction").onField("merchant_account_id").get(0).getCode());
    }

    @Test
    public void invalidMerchantInitiatedRequestType() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.merchantInitiatedRequestType("foo");
        request.priorAuthenticationId("threedsecureverification");

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);
        assertEquals("Merchant Initiated Request Type is invalid.", outerResult.getErrors().getAllValidationErrors().get(0).getMessage());
    }

    @Test
    public void merchantOnRecordNameNotPassedForPaymentWithMultipleMerchants() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.merchantInitiatedRequestType("payment_with_multiple_merchants");
        request.priorAuthenticationId("threedsecureverification");

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);
        assertEquals("Merchant on record name is needed for 3RI payment with multiple merchants.", outerResult.getErrors().getAllValidationErrors().get(0).getMessage());
    }

    @Test
    public void valid3RIPaymentWithMultipleMerchants() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.merchantInitiatedRequestType("payment_with_multiple_merchants");
        request.priorAuthenticationId("threedsecureverification");
        request.merchantOnRecordName("merchant123");

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);
        assertNull(outerResult.getErrors());
    }

    @Test
    public void valid3RIFields() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("10.00");
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.merchantInitiatedRequestType("split_shipment");
        request.priorAuthenticationId("threedsecureverification");

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);
        assertNull(outerResult.getErrors());
    }

    @Test
    public void valid3RIPriorAuthenticationDetails() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4000000000001091").
            expirationMonth("12").
            expirationYear("2030");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        Calendar authTime = Calendar.getInstance();
        authTime.set(2024, Calendar.FEBRUARY, 10, 22, 45, 30);

        ThreeDSecureLookupPriorAuthenticationDetails priorAuthenticationDetails = new ThreeDSecureLookupPriorAuthenticationDetails()
            .acsTransactionId("acs-transaction-id")
            .authenticationMethod("01")
            .authenticationTime(authTime)
            .dsTransactionId("ds-transaction-id");

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.clientData(clientData);
        request.email("first.last@example.com");
        request.merchantInitiatedRequestType("split_shipment");
        request.amount("10.00");
        request.priorAuthenticationDetails(priorAuthenticationDetails);

        setDeviceDataFields(request);

        Result<ThreeDSecureLookupResponse> outerResult = gateway.threeDSecure().lookup(request);
        assertNull(outerResult.getErrors());
    }

    public String getClientDataString(String nonce, String authorizationFingerprint) {
        return "{\n" +
                "\"authorizationFingerprint\": \"" + authorizationFingerprint + "\",\n" +
                "\"braintreeLibraryVersion\": \"braintree/web/3.44.0\",\n" +
                "\"dfReferenceId\": \"ABC-123\",\n" +
                "\"nonce\": \"" + nonce + "\",\n" +
                "\"clientMetadata\": {\n" +
                "\"cardinalDeviceDataCollectionTimeElapsed\": 40,\n" +
                "\"issuerDeviceDataCollectionResult\": true,\n" +
                "\"issuerDeviceDataCollectionTimeElapsed\": 413,\n" +
                "\"requestedThreeDSecureVersion\": \"2\",\n" +
                "\"sdkVersion\": \"web/3.44.0\"\n" +
                "}\n" +
                "}";
    }

    private void setDeviceDataFields(ThreeDSecureLookupRequest request) {
        request.browserJavaEnabled(false);
        request.browserAcceptHeader("text/html;q=0.8");
        request.browserLanguage("fr-CA");
        request.browserColorDepth("48");
        request.browserScreenHeight("600");
        request.browserScreenWidth("800");
        request.browserTimeZone("-60");
        request.userAgent("Mozilla/5.0");
        request.ipAddress("2001:0db8:0000:0000:0000:ff00:0042:8329");
        request.deviceChannel("Browser");
        request.browserJavascriptEnabled(true);
    }
}
