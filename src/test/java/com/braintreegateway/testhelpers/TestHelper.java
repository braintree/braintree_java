package com.braintreegateway.testhelpers;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.braintreegateway.*;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.*;
import com.fasterxml.jackson.jr.ob.JSON;

import org.junit.jupiter.api.Disabled;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TestHelper {

    public static final class CompareModificationsById implements Comparator<Modification> {
        public int compare(Modification left, Modification right) {
            return left.getId().compareTo(right.getId());
        }
    }

    public static void assertDatesEqual(Calendar first, Calendar second) {
        if (first == null && second != null) {
            throw new AssertionError("dates are not equal. first is null, second is not");
        } else if (first != null && second == null) {
            throw new AssertionError("dates are not equal. second is null, first is not");
        }
        boolean yearsNotEqual = first.get(Calendar.YEAR) != second.get(Calendar.YEAR);
        boolean monthsNotEqual = first.get(Calendar.MONTH) != second.get(Calendar.MONTH);
        boolean daysNotEqual = first.get(Calendar.DAY_OF_MONTH) != second.get(Calendar.DAY_OF_MONTH);
        if (yearsNotEqual || monthsNotEqual || daysNotEqual) {
            StringBuffer buffer = new StringBuffer("dates are not equal. ");
            if (yearsNotEqual) {
                buffer.append("years (" + first.get(Calendar.YEAR) + ", " + second.get(Calendar.YEAR) + ") not equal.");
            }
            if (monthsNotEqual) {
                buffer.append("months (" + first.get(Calendar.MONTH) + ", " + second.get(Calendar.MONTH) + ") not equal.");
            }
            if (daysNotEqual) {
                buffer.append("days (" + first.get(Calendar.DAY_OF_MONTH) + ", " + second.get(Calendar.DAY_OF_MONTH) + ") not equal.");
            }
            throw new AssertionError(buffer.toString());
        }
    }

    public static void assertIncludes(String expected, String all) {
        assertTrue(all.indexOf(expected) >= 0, "Expected:\n" + all + "\nto include:\n" + expected);
    }

    public static boolean listIncludes(List<? extends Object> list, Object expectedItem) {
        for (Object item : list) {
            if (item.equals(expectedItem)) {
                return true;
            }
        }
        return false;
    }

    public static boolean includesSubscription(ResourceCollection<Subscription> collection, Subscription item) {
        for (Subscription subscription : collection) {
            if (subscription.getId().equals(item.getId())) {
                return true;
            }
        }

        return false;
    }

    public static boolean includesStatus(ResourceCollection<Transaction> collection, Status status) {
        for (Transaction transaction : collection) {
            if (transaction.getStatus().equals(status)) {
                return true;
            }
        }

        return false;
    }

    public static Result<Transaction> settle(BraintreeGateway gateway, String transactionId) {
      return gateway.testing().settle(transactionId);
    }

    public static Result<Transaction> settlement_confirm(BraintreeGateway gateway, String transactionId) {
      return gateway.testing().settlementConfirm(transactionId);
    }

    public static Result<Transaction> settlement_decline(BraintreeGateway gateway, String transactionId) {
      return gateway.testing().settlementDecline(transactionId);
    }

    public static void escrow(BraintreeGateway gateway, String transactionId) {
        NodeWrapper response = new Http(gateway.getConfiguration()).put(gateway.getConfiguration().getMerchantPath() + "/transactions/" + transactionId + "/escrow");
        assertTrue(response.isSuccess());
    }

    public static String createTest3DS(BraintreeGateway gateway, String merchantAccountId, ThreeDSecureRequestForTests request) {
        String url = gateway.getConfiguration().getMerchantPath() + "/three_d_secure/create_verification/" + merchantAccountId;
        NodeWrapper response = new Http(gateway.getConfiguration()).post(url, request);
        assertTrue(response.isSuccess());

        String token = response.findString("three-d-secure-authentication-id");
        assertNotNull(token);
        return token;
    }

    public static String generateUnlockedNonce(BraintreeGateway gateway, String customerId, String creditCardNumber) {
      ClientTokenRequest request = new ClientTokenRequest();
      if (customerId != null) {
          request = request.customerId(customerId);
      }
      String encodedClientToken = gateway.clientToken().generate(request);
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
      QueryString payload = new QueryString();
      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "test-identifier").
        append("credit_card[number]", creditCardNumber).
        append("credit_card[expiration_month]", "11").
        append("share", "true").
        append("credit_card[expiration_year]", "2099");

      String responseBody;
      String nonce = "";
      try {
        responseBody = HttpHelper.post(url, payload.toString());
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String generateThreeDSecureNonce(BraintreeGateway gateway, CreditCardRequest creditCardRequest) {
        String merchantAccountId = MerchantAccountTestConstants.THREE_D_SECURE_MERCHANT_ACCOUNT_ID;
        String url = gateway.getConfiguration().getMerchantPath() + "/three_d_secure/create_nonce/" + merchantAccountId;
        NodeWrapper response = new Http(gateway.getConfiguration()).post(url, creditCardRequest);
        assertTrue(response.isSuccess());

        String nonce = response.findString("nonce");
        assertNotNull(nonce);
        return nonce;
    }

    public static String decodeClientToken(String rawClientToken) {
        String decodedClientToken = new String(Base64.decodeBase64(rawClientToken), Charset.forName("UTF-8"));
        return decodedClientToken.replace("\\u0026", "&");
    }

    public static String generateOneTimePayPalNonce(BraintreeGateway gateway) {
      String encodedClientToken = gateway.clientToken().generate();
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/paypal_accounts";
      QueryString payload = new QueryString();
      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "test-identifier").
        append("paypal_account[access_token]", "access").
        append("paypal_account[options][validate]", "false");

      String responseBody;
      String nonce = "";
      try {
        responseBody = HttpHelper.post(url, payload.toString());
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String generateOrderPaymentPayPalNonce(BraintreeGateway gateway) {
        QueryString payload = new QueryString();
        payload.append("paypal_account[intent]", "order");
        payload.append("paypal_account[payment_token]", "fake_payment_token");
        payload.append("paypal_account[payer_id]", "fake_payer_id");

        return generatePayPalNonce(gateway, payload);
    }

    public static String generateAuthorizationFingerprint (BraintreeGateway gateway, String customerId) {
      ClientTokenRequest clientTokenRequest = new ClientTokenRequest().
        customerId(customerId);

      String encodedClientToken = gateway.clientToken().generate(clientTokenRequest);
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);

      return authorizationFingerprint;
    }

    public static String generateNonceForCreditCard(BraintreeGateway gateway, CreditCardRequest creditCardRequest, String customerId, boolean validate) {
      ClientTokenRequest clientTokenRequest = new ClientTokenRequest().
        customerId(customerId);

      String encodedClientToken = gateway.clientToken().generate(clientTokenRequest);
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/credit_cards";
      QueryString payload = new QueryString();
      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "fake_identifier").
        append("credit_card[options][validate]", Boolean.valueOf(validate).toString());

      String responseBody;
      String nonce = "";
      try {
        String payloadString = payload.toString();
        payloadString += "&" + creditCardRequest.toQueryString();
        responseBody = HttpHelper.post(url, payloadString);
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String getNonceForPayPalAccount(BraintreeGateway gateway, String consentCode) {
        return getNonceForPayPalAccount(gateway, consentCode, null);
    }

    public static String getNonceForPayPalAccount(BraintreeGateway gateway, String consentCode, String token) {
      String encodedClientToken = gateway.clientToken().generate();
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/paypal_accounts";
      QueryString payload = new QueryString();
      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "test-identifier").
        append("paypal_account[consent_code]", consentCode).
        append("paypal_account[token]", token).
        append("paypal_account[options][validate]", "false");

      String responseBody;
      String nonce = "";
      try {
        responseBody = HttpHelper.post(url, payload.toString());
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String generateSepaDebitNonce(BraintreeGateway gateway) {
      String encodedClientToken = gateway.clientToken().generate();
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/sepa_debit_accounts";

      QueryString payload = new QueryString();

      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("sepa_debit_account[last_4]", "1234").
        append("sepa_debit_account[merchant_or_partner_customer_id]", "a-mp-customer-id").
        append("sepa_debit_account[bank_reference_token]", "123456789").
        append("sepa_debit_account[mandate_type]", "RECURRENT");

      String responseBody;
      String nonce = "";
      try {
        responseBody = HttpHelper.post(url, payload.toString());
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String generateFuturePaymentPayPalNonce(BraintreeGateway gateway) {
      QueryString payload = new QueryString();
      payload.append("paypal_account[consent_code]", "consent");

      return generatePayPalNonce(gateway, payload);
    }

    private static String generatePayPalNonce(BraintreeGateway gateway, QueryString payload) {
      String encodedClientToken = gateway.clientToken().generate();
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      Configuration configuration = gateway.getConfiguration();
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/payment_methods/paypal_accounts";

      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "test-identifier").
        append("paypal_account[options][validate]", "false");

      String responseBody;
      String nonce = "";
      try {
        responseBody = HttpHelper.post(url, payload.toString());
        nonce = extractParamFromJson("nonce", responseBody);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return nonce;
    }

    public static String generateUnlockedNonce(BraintreeGateway gateway) {
        return generateUnlockedNonce(gateway, null, "4111111111111111");
    }

    public static String extractParamFromJson(String keyName, String json) {
        String regex = "\"" + keyName + "\":\\s*\"([^\"]+)\"";
        Pattern keyPattern = Pattern.compile(regex);
        Matcher m = keyPattern.matcher(json);

        String value = "";
        if (m.find()) {
          value = m.group(1);
        }

        return value;
    }

    public static int extractIntParamFromJson(String keyName, String json) {
        String regex = "\"" + keyName + "\":\\s*(\\d+)";
        Pattern keyPattern = Pattern.compile(regex);
        Matcher m = keyPattern.matcher(json);

        int value = 0;
        if (m.find()) {
          value = Integer.parseInt(m.group(1));
        }

        return value;
    }

    public static final class OAuthGrantRequest extends Request {

        private String scope;
        private String merchantId;

        public OAuthGrantRequest scope(String scope) {
            this.scope = scope;
            return this;
        }

        public OAuthGrantRequest merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        @Override
        public String toXML() {
            return new RequestBuilder("grant").
                addElement("scope", scope).
                addElement("merchant_public_id", merchantId).
                toXML();
        }
    }

    public static String createOAuthGrant(BraintreeGateway gateway, String merchantId, String scope) {
        Http http = new Http(gateway.getConfiguration());
        OAuthGrantRequest request = new OAuthGrantRequest().
            scope(scope).
            merchantId(merchantId);

        NodeWrapper node = http.post("/oauth_testing/grants", request);
        return node.findString("code");
    }

    /* http://stackoverflow.com/questions/13592236/parse-the-uri-string-into-name-value-collection-in-java */
    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
            if (queryPairs.get(key) == null) {
                queryPairs.put(key, URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
            else {
                queryPairs.put(key, queryPairs.get(key) + ", " + URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return queryPairs;
    }

    public static String generateValidUsBankAccountNonce(BraintreeGateway gateway) {
        return generateValidUsBankAccountNonce(gateway,"567891234");
    }

    public static String generateValidUsBankAccountNonce(BraintreeGateway gateway, String accountNumber) {
        String mutation =
            "mutation TokenizeUsBankAccount($input: TokenizeUsBankAccountInput!) {"
            + "    tokenizeUsBankAccount(input: $input) {"
            + "        paymentMethod {"
            + "            id"
            + "        }"
            + "    }"
            + "}";

        String input =
            "{\n"
            + "  \"input\": {\n"
            + "    \"usBankAccount\": {\n"
            + "      \"accountNumber\": " + Integer.parseInt(accountNumber) + ",\n"
            + "      \"routingNumber\": \"021000021\",\n"
            + "      \"accountType\": \"CHECKING\",\n"
            + "      \"individualOwner\": {\n"
            + "        \"firstName\": \"Dan\",\n"
            + "        \"lastName\": \"Schulman\"\n"
            + "      },\n"
            + "      \"billingAddress\": {\n"
            + "        \"streetAddress\": \"123 Ave\",\n"
            + "        \"state\": \"CA\",\n"
            + "        \"city\": \"San Francisco\",\n"
            + "        \"zipCode\": \"94112\"\n"
            + "      },\n"
            + "      \"achMandate\": \"cl mandate text\"\n"
            + "    }\n"
            + "  }\n"
            + "}";

        Map<String, Object> variables;
        try {
            variables = JSON.std.mapFrom(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = sendGraphQLRequest(gateway, GraphQLClient.formatGraphQLRequest(mutation, variables));
        Map<String, Object> data = (Map) response.get("data");
        Map<String, Object> tokenizeUsBankAccount = (Map) data.get("tokenizeUsBankAccount");
        Map<String, String> paymentMethod = (Map) tokenizeUsBankAccount.get("paymentMethod");
        return paymentMethod.get("id");
    }

    public static String generateValidUsBankAccountNonceForBusiness(BraintreeGateway gateway) {
        return generateValidUsBankAccountNonceForBusiness(gateway,"567891234");
    }

    public static String generateValidUsBankAccountNonceForBusiness(BraintreeGateway gateway, String accountNumber) {
        String mutation =
            "mutation TokenizeUsBankAccount($input: TokenizeUsBankAccountInput!) {"
            + "    tokenizeUsBankAccount(input: $input) {"
            + "        paymentMethod {"
            + "            id"
            + "        }"
            + "    }"
            + "}";

        String input =
            "{\n"
            + "  \"input\": {\n"
            + "    \"usBankAccount\": {\n"
            + "      \"accountNumber\": " + Integer.parseInt(accountNumber) + ",\n"
            + "      \"routingNumber\": \"021000021\",\n"
            + "      \"accountType\": \"CHECKING\",\n"
            + "      \"businessOwner\": {\n"
            + "        \"businessName\": \"Big Tech\"\n"
            + "      },\n"
            + "      \"billingAddress\": {\n"
            + "        \"streetAddress\": \"123 Ave\",\n"
            + "        \"state\": \"CA\",\n"
            + "        \"city\": \"San Francisco\",\n"
            + "        \"zipCode\": \"94112\"\n"
            + "      },\n"
            + "      \"achMandate\": \"cl mandate text\"\n"
            + "    }\n"
            + "  }\n"
            + "}";

        Map<String, Object> variables;
        try {
            variables = JSON.std.mapFrom(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = sendGraphQLRequest(gateway, GraphQLClient.formatGraphQLRequest(mutation, variables));
        Map<String, Object> data = (Map) response.get("data");
        Map<String, Object> tokenizeUsBankAccount = (Map) data.get("tokenizeUsBankAccount");
        Map<String, String> paymentMethod = (Map) tokenizeUsBankAccount.get("paymentMethod");
        return paymentMethod.get("id");
    }

    public static String generatePlaidUsBankAccountNonce(BraintreeGateway gateway) {
        String mutation =
            "mutation TokenizeUsBankLogin($input: TokenizeUsBankLoginInput!) {"
            + "    tokenizeUsBankLogin(input: $input) {"
            + "        paymentMethod {"
            + "            id"
            + "        }"
            + "    }"
            + "}";

        String input =
            "{\n"
            + "  \"input\": {\n"
            + "    \"usBankLogin\": {\n"
            + "      \"accountId\": \"plaid_account_id\",\n"
            + "      \"publicToken\": \"good\",\n"
            + "      \"accountType\": \"CHECKING\",\n"
            + "      \"individualOwner\": {\n"
            + "        \"firstName\": \"Dan\",\n"
            + "        \"lastName\": \"Schulman\"\n"
            + "      },\n"
            + "      \"billingAddress\": {\n"
            + "        \"streetAddress\": \"123 Ave\",\n"
            + "        \"state\": \"CA\",\n"
            + "        \"city\": \"San Francisco\",\n"
            + "        \"zipCode\": \"94112\"\n"
            + "      },\n"
            + "      \"achMandate\": \"cl mandate text\"\n"
            + "    }\n"
            + "  }\n"
            + "}";

        Map<String, Object> variables;
        try {
            variables = JSON.std.mapFrom(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> response = sendGraphQLRequest(gateway, GraphQLClient.formatGraphQLRequest(mutation, variables));
        Map<String, Object> data = (Map) response.get("data");
        Map<String, Object> tokenizeUsBankLogin = (Map) data.get("tokenizeUsBankLogin");
        Map<String, String> paymentMethod = (Map) tokenizeUsBankLogin.get("paymentMethod");
        return paymentMethod.get("id");
    }

    public static String generateInvalidUsBankAccountNonce() {
        String valid_characters = "bcdfghjkmnpqrstvwxyz23456789";
        String token = "tokenusbankacct";
        for (int i=0; i < 4; i++) {
            token += '_';
            for (int j=0; j<6; j++) {
                Integer pick = new Random().nextInt(valid_characters.length());
                token += valid_characters.charAt(pick);
            }
        }
        return token + "_xxx";
    }
    
    private static Map<String, Object> sendGraphQLRequest(BraintreeGateway gateway, String graphQLRequest) {
        String encodedClientToken = gateway.clientToken().generate();
        String clientToken = TestHelper.decodeClientToken(encodedClientToken);

        Map<String, Object> response;
        try {
            Map<String, Object> clientTokenJson = JSON.std.mapFrom(clientToken);
            Map<String, Object> braintreeApi = (Map<String, Object>) clientTokenJson.get("braintree_api");
            URL url = new URL(braintreeApi.get("url") + "/graphql");

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, null, null);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
              ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
            }
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Braintree-Version", "2016-10-07");
            connection.addRequestProperty("Authorization", "Bearer " + braintreeApi.get("access_token"));
            connection.setDoOutput(true);
            connection.getOutputStream().write(graphQLRequest.getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().close();

            InputStream responseStream = connection.getInputStream();
            String body = StringUtils.inputStreamToString(responseStream);
            responseStream.close();
            response  = JSON.std.mapFrom(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
