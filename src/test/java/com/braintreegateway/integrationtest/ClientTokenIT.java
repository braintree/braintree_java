package com.braintreegateway.integrationtest;

import java.net.URLEncoder;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.braintreegateway.testhelpers.HttpHelper;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.*;

public class ClientTokenIT extends IntegrationTest {
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

    private String _getFingerprint(String rawClientToken) {
        String decodedClientToken = TestHelper.decodeClientToken(rawClientToken);
        return TestHelper.extractParamFromJson("authorizationFingerprint", decodedClientToken);
    }

    @Test
    public void fingerprintIsAcceptedByTheGateway() {
        String clientToken = gateway.clientToken().generate();
        String authorizationFingerprint = _getFingerprint(clientToken);
        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sharedCustomerIdentifierType=testing&sharedCustomerIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.getResponseCode(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }

    @Test
    public void versionOptionSupported() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest().version(1);
        String clientToken = gateway.clientToken().generate(clientTokenRequest);
        int version = TestHelper.extractIntParamFromJson("version", clientToken);
        assertEquals(1, version);
    }

    @Test
    public void versionDefaultsToTwo() {
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest();
        String encodedClientToken = gateway.clientToken().generate(clientTokenRequest);
        String decodedClientToken = TestHelper.decodeClientToken(encodedClientToken);
        int version = TestHelper.extractIntParamFromJson("version", decodedClientToken);
        assertEquals(2, version);
    }

    @Test
    public void versionDefaultsToTwoWithoutRequest() {
        String encodedClientToken = gateway.clientToken().generate();
        String decodedClientToken = TestHelper.decodeClientToken(encodedClientToken);
        int version = TestHelper.extractIntParamFromJson("version", decodedClientToken);
        assertEquals(2, version);
    }

    @Test
    public void fingerprintCanContainCustomerId() {
        CustomerRequest customerRequest = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(customerRequest);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
            .customerId(customer.getId());
        String clientToken = gateway.clientToken().generate(clientTokenRequest);

        String authorizationFingerprint = _getFingerprint(clientToken);
        String encodedFingerprint = "";
        try {
            encodedFingerprint = URLEncoder.encode(authorizationFingerprint, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods?";
        url += "authorizationFingerprint=" + encodedFingerprint;
        url += "&sharedCustomerIdentifierType=testing&sharedCustomerIdentifier=test-identifier";

        int responseCode = -1;
        try {
          responseCode = HttpHelper.getResponseCode(url);
        } catch (java.net.MalformedURLException e) {
          fail();
        } catch (java.io.IOException e) {
          fail();
        }
        assertEquals(200, responseCode);
    }

    @Test
    public void gatewayRespectsVerifyCard() {
        CustomerRequest customerRequest = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(customerRequest);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
            .customerId(customer.getId())
            .options(new ClientTokenOptionsRequest().verifyCard(true));
        String clientToken = gateway.clientToken().generate(clientTokenRequest);

        String authorizationFingerprint = _getFingerprint(clientToken);

        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("shared_customer_identifier_type", "testing").
            append("shared_customer_identifier", "test-identifier").
            append("credit_card[number]", "4000111111111115").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        int responseCode = postResponseCode(url, payload);
        assertEquals(422, responseCode);

        int new_card_count = gateway.customer().find(customer.getId()).getCreditCards().size();
        assertEquals(0, new_card_count);
    }

    @Test
    public void gatewayRespectsMakeDefault() {
        CustomerRequest customerRequest = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(customerRequest);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        CreditCardRequest creditCardRequest = new CreditCardRequest()
            .customerId(customer.getId())
            .number("5105105105105100")
            .expirationDate("11/2099");

        Result<CreditCard> creditCardResult = gateway.creditCard().create(creditCardRequest);
        assertTrue(creditCardResult.isSuccess());

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
            .customerId(customer.getId())
            .options(new ClientTokenOptionsRequest().makeDefault(true));
        String clientToken = gateway.clientToken().generate(clientTokenRequest);

        String authorizationFingerprint = _getFingerprint(clientToken);

        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("shared_customer_identifier_type", "testing").
            append("shared_customer_identifier", "test-identifier").
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

    @Test
    public void gatewayRespectsFailOnDuplicatePaymentMethod() {
        CustomerRequest customerRequest = new CustomerRequest();
        Result<Customer> result = gateway.customer().create(customerRequest);
        assertTrue(result.isSuccess());
        Customer customer = result.getTarget();

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
            .customerId(customer.getId())
            .options(new ClientTokenOptionsRequest().failOnDuplicatePaymentMethod(false));
        String clientToken = gateway.clientToken().generate(clientTokenRequest);

        String authorizationFingerprint = _getFingerprint(clientToken);

        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("shared_customer_identifier_type", "testing").
            append("shared_customer_identifier", "test-identifier").
            append("credit_card[number]", "4111111111111111").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        int responseCode = postResponseCode(url, payload);
        assertEquals(201, responseCode);

        clientTokenRequest = new ClientTokenRequest()
            .customerId(customer.getId())
            .options(new ClientTokenOptionsRequest().failOnDuplicatePaymentMethod(true));
        clientToken = gateway.clientToken().generate(clientTokenRequest);

        authorizationFingerprint = _getFingerprint(clientToken);

        url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
        payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint).
            append("shared_customer_identifier_type", "testing").
            append("shared_customer_identifier", "test-identifier").
            append("credit_card[number]", "4111111111111111").
            append("credit_card[expiration_month]", "11").
            append("credit_card[expiration_year]", "2099");

        responseCode = postResponseCode(url, payload);
        assertEquals(422, responseCode);

        int new_card_count = gateway.customer().find(customer.getId()).getCreditCards().size();
        assertEquals(1, new_card_count);
    }
}
