package com.braintreegateway.integrationtest;

import java.util.regex.Pattern;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.BraintreeException;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreeDSecureIT extends IntegrationTest implements MerchantAccountTestConstants {

    @Test
    public void lookupThreeDSecure() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");

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

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);

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
    public void lookupThreeDSecure_partialCustomerInfo() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        CreditCardRequest cardRequest = new CreditCardRequest().
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);
        request.email("first.last@example.com");

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);

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
            number("4111111111111111").
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateNonceForCreditCard(gateway, cardRequest, customer.getId(), false);
        String authorizationFingerprint = TestHelper.generateAuthorizationFingerprint(gateway, customer.getId());

        String clientData = getClientDataString(nonce, authorizationFingerprint);

        ThreeDSecureLookupRequest request = new ThreeDSecureLookupRequest();
        request.amount("199.00");
        request.clientData(clientData);

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);

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
    public void lookupThreeDSecure_thowsBraintreeException_forInvalidAuthrizationFingerprint() {
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
            ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);
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

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

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

        ThreeDSecureLookupResponse result = gateway.threeDSecure().lookup(request);

        PaymentMethodNonce paymentMethod = result.getPaymentMethod();
        ThreeDSecureLookup lookup = result.getLookup();

        assertNotNull(result.getPayloadString());
        assertNotNull(paymentMethod.getNonce());

        TransactionRequest transactionRequest = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(DEFAULT_MERCHANT_ACCOUNT_ID).
            paymentMethodNonce(paymentMethod.getNonce());
        Result<Transaction> transactionResult = gateway.transaction().sale(transactionRequest);
        assertFalse(transactionResult.isSuccess());
        System.out.println(transactionResult.getErrors().forObject("transaction"));
        assertEquals(ValidationErrorCode.TRANSACTION_MERCHANT_ACCOUNT_DOES_NOT_MATCH3_D_SECURE_MERCHANT_ACCOUNT,
            transactionResult.getErrors().forObject("transaction").onField("merchant_account_id").get(0).getCode());
    }

    public String getClientDataString(String nonce, String authorizationFingerprint) {
        return new StringBuilder()
          .append("{\n")
            .append("\"authorizationFingerprint\": \"" + authorizationFingerprint + "\",\n")
            .append("\"braintreeLibraryVersion\": \"braintree/web/3.44.0\",\n")
            .append("\"dfReferenceId\": \"ABC-123\",\n")
            .append("\"nonce\": \"" + nonce + "\",\n")
            .append("\"clientMetadata\": {\n")
                .append("\"cardinalDeviceDataCollectionTimeElapsed\": 40,\n")
                .append("\"issuerDeviceDataCollectionResult\": true,\n")
                .append("\"issuerDeviceDataCollectionTimeElapsed\": 413,\n")
                .append("\"requestedThreeDSecureVersion\": \"2\",\n")
                .append("\"sdkVersion\": \"web/3.44.0\"\n")
            .append("}\n")
          .append("}")
        .toString();
    }
}
