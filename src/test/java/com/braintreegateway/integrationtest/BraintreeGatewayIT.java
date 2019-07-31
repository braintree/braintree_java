package com.braintreegateway.integrationtest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class BraintreeGatewayIT extends IntegrationTest {

    @Test
    public void developmentBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "publicKey", "privateKey");
        String port = System.getenv().get("GATEWAY_PORT") == null ? "3000" : System.getenv().get("GATEWAY_PORT");
        Configuration configuration = gateway.getConfiguration();
        assertEquals("http://localhost:" + port + "/merchants/integration_merchant_id", configuration.getBaseURL() + configuration.getMerchantPath());
    }

    @Test
    public void qaBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.QA, "qa_merchant_id", "publicKey", "privateKey");
        Configuration configuration = gateway.getConfiguration();
        assertEquals("https://gateway.qa.braintreepayments.com:443/merchants/qa_merchant_id", configuration.getBaseURL() + configuration.getMerchantPath());
    }

    @Test
    public void sandboxBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "sandbox_merchant_id", "publicKey", "privateKey");
        Configuration configuration = gateway.getConfiguration();
        assertEquals("https://api.sandbox.braintreegateway.com:443/merchants/sandbox_merchant_id", configuration.getBaseURL() + configuration.getMerchantPath());
    }

    @Test
    public void productionBaseMerchantUrl() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "production_merchant_id", "publicKey", "privateKey");
        Configuration configuration = gateway.getConfiguration();
        assertEquals("https://api.braintreegateway.com:443/merchants/production_merchant_id", configuration.getBaseURL() + configuration.getMerchantPath());
    }

    @Test
    public void tokenizeRawCreditCardDetailsWithGraphQL() {
        String query = "mutation ExampleServerSideSingleUseToken($input: TokenizeCreditCardInput!) {" +
        "  tokenizeCreditCard(input: $input) {" +
        "    paymentMethod {" +
        "      id" +
        "      usage" +
        "      details {" +
        "        ... on CreditCardDetails {" +
        "          bin" +
        "          brandCode" +
        "          last4" +
        "          expirationYear" +
        "          expirationMonth" +
        "        }" +
        "      }" +
        "    }" +
        "  }" +
        "}";
        Map<String, Object> variables = new HashMap<String, Object>();
        Map<String, Object> input = new HashMap<String, Object>();
        Map<String, Object> creditCard = new HashMap<String, Object>();

        creditCard.put("number", "4005519200000004");
        creditCard.put("expirationYear", "2024");
        creditCard.put("expirationMonth", "05");
        creditCard.put("cardholderName", "Joe Bloggs");

        input.put("creditCard", creditCard);
        variables.put("input", input);

        Map<String, Object> result = (Map) gateway.graphQLClient.query(query, variables).get("data");
        Map<String, Object> tokenizedCard = (Map) result.get("tokenizeCreditCard");
        Map<String, Object> paymentMethod = (Map) tokenizedCard.get("paymentMethod");
        Map<String, Object> details = (Map) paymentMethod.get("details");

        assertNotNull(paymentMethod.get("id"));
        assertEquals(details.get("bin"), "400551");
        assertEquals(details.get("last4"), "0004");
        assertEquals(details.get("brandCode"), "VISA");
        assertEquals(details.get("expirationMonth"), "05");
        assertEquals(details.get("expirationYear"), "2024");
    }

    @Test
    public void pingGraphQLWithoutVariables() {
        String query = "query ExampleQuery {" +
        "  ping" +
        "}";

        Map<String, Object> result = (Map) gateway.graphQLClient.query(query).get("data");

        assertEquals(result.get("ping"), "pong");
    }
}
