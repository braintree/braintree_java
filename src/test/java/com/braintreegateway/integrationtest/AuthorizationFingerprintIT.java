package com.braintreegateway.integrationtest;

import java.net.URLEncoder;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.braintreegateway.testhelpers.HttpHelper;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.CreditCardRequest;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Result;
import com.braintreegateway.Environment;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.AuthorizationFingerprintOptions;

public class AuthorizationFingerprintIT {
    private BraintreeGateway gateway;

    private String urlencode(String string) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(string, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encodedString;
    }

    private int postResponseCode(String url, QueryString payload) {
        int responseCode = -1;
        try {
          responseCode = HttpHelper.postResponseCode(url, payload.toString());
        } catch (java.net.MalformedURLException e) {
          throw new RuntimeException(e);
        } catch (java.io.IOException e) {
          throw new RuntimeException(e);
        }

        return responseCode;
    }

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration_merchant_id",
            "integration_public_key",
            "integration_private_key"
        );
    }

    @Test
    public void fingerprintIsAcceptedByTheGateway() {
        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions();
        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);
        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sessionIdentifierType=testing&sessionIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.get(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }

    @Test
    public void fingerprintWithCreditCardOptionsIsAccepted() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions().verifyCard(true).
          customerId(customer.getId());
        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);
        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sessionIdentifierType=testing&sessionIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.get(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }

    @Test
    public void fingerprintCanContainCustomerId() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions().
          customerId(customer.getId());
        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);

        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sessionIdentifierType=testing&sessionIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.get(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }

    @Test
    public void fingerprintContainsVerifyCard() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions().
          verifyCard(true).
          customerId(customer.getId());

        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("session_identifier_type", "testing").
            append("session_identifier", "test-identifier").
            append("credit_card[number]", "4000111111111115").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        int responseCode = postResponseCode(url, payload);
        assertEquals(422, responseCode);

        int new_card_count = gateway.customer().find(customer.getId()).getCreditCards().size();
        assertEquals(0, new_card_count);
    }

    @Test
    public void fingerprintContainsFailOnDuplicatePaymentMethod() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions().
          failOnDuplicatePaymentMethod(false).
          customerId(customer.getId());

        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("session_identifier_type", "testing").
            append("session_identifier", "test-identifier").
            append("credit_card[number]", "4111111111111111").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        int responseCode = postResponseCode(url, payload);
        assertEquals(201, responseCode);

        authorizationFingerprintOptions = new AuthorizationFingerprintOptions().
          failOnDuplicatePaymentMethod(true).
          customerId(customer.getId());

        authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);

        url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json";
        payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("session_identifier_type", "testing").
            append("session_identifier", "test-identifier").
            append("credit_card[number]", "4111111111111111").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        responseCode = postResponseCode(url, payload);
        assertEquals(422, responseCode);

        int new_card_count = gateway.customer().find(customer.getId()).getCreditCards().size();
        assertEquals(1, new_card_count);
    }

    @Test
    public void fingerprintContainsMakeDefault() {
        CustomerRequest request = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(request);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest()
            .customerId(customer.getId())
            .number("5105105105105100")
            .expirationDate("11/2099");

        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        AuthorizationFingerprintOptions authorizationFingerprintOptions = new AuthorizationFingerprintOptions().
          makeDefault(true).
          customerId(customer.getId());

        String authorizationFingerprint = gateway.generateAuthorizationFingerprint(authorizationFingerprintOptions);

        String url = Environment.DEVELOPMENT.baseURL + "/client_api/credit_cards.json";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("session_identifier_type", "testing").
            append("session_identifier", "test-identifier").
            append("credit_card[number]", "4111111111111111").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        int responseCode = postResponseCode(url, payload);
        assertEquals(201, responseCode);

        List<CreditCard> creditCards = gateway.customer().find(customer.getId()).getCreditCards();
        assertEquals(2, creditCards.size());
        for (CreditCard creditCard : creditCards) {
          if (creditCard.getLast4().equals("1111")) {
            assertTrue(creditCard.isDefault());
          }
        }
    }
}
