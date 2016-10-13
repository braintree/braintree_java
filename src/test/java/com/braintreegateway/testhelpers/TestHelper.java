package com.braintreegateway.testhelpers;

import com.braintreegateway.*;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Sha1Hasher;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.util.StringUtils;
import com.braintreegateway.EuropeBankAccount.MandateType;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;

import org.junit.Ignore;
import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

@Ignore("Testing utility class")
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
        assertTrue("Expected:\n" + all + "\nto include:\n" + expected, all.indexOf(expected) >= 0);
    }

    public static void assertValidTrData(Configuration configuration, String trData) {
        String[] dataSections = trData.split("\\|");
        String trHash = dataSections[0];
        String trContent = dataSections[1];
        assertEquals(trHash, new Sha1Hasher().hmacHash(configuration.getPrivateKey(), trContent));
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

        String token = response.findString("three-d-secure-token");
        assertNotNull(token);
        return token;
    }

    public static String simulateFormPostForTR(BraintreeGateway gateway, Request trParams, Request request, String postUrl) {
        String response = "";
        try {
            String trData = gateway.transparentRedirect().trData(trParams, "http://example.com");
            StringBuilder postData = new StringBuilder("tr_data=")
                    .append(URLEncoder.encode(trData, "UTF-8"))
                    .append("&")
                    .append(request.toQueryString());

            URL url = new URL(postUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/xml");
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.getOutputStream().write(postData.toString().getBytes("UTF-8"));
            connection.getOutputStream().close();
            if (connection.getResponseCode() == 422) {
                connection.getErrorStream();
            } else {
                connection.getInputStream();
            }

            response = new URL(connection.getHeaderField("Location")).getQuery();
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage());
        }

        return response;
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
      String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/nonces.json";
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
        append("credit_card[options][validate]", new Boolean(validate).toString());

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

    public static String generateEuropeBankAccountNonce(BraintreeGateway gateway, Customer customer) {
        SEPAClientTokenRequest request = new SEPAClientTokenRequest();
        request.customerId(customer.getId());
        request.mandateType(EuropeBankAccount.MandateType.BUSINESS);
        request.mandateAcceptanceLocation("Rostock, Germany");

        String encodedClientToken = gateway.clientToken().generate(request);
        String clientToken = TestHelper.decodeClientToken(encodedClientToken);

        String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
        Configuration configuration = gateway.getConfiguration();
        String url = configuration.getBaseURL() + configuration.getMerchantPath() + "/client_api/v1/sepa_mandates";
        QueryString payload = new QueryString();
        payload.append("authorization_fingerprint", authorizationFingerprint)
              .append("sepa_mandate[locale]", "de-DE")
              .append("sepa_mandate[bic]", "DEUTDEFF")
              .append("sepa_mandate[iban]", "DE89370400440532013000")
              .append("sepa_mandate[accountHolderName]", "Bob Holder")
              .append("sepa_mandate[billingAddress][streetAddress]", "123 Currywurst Way")
              .append("sepa_mandate[billingAddress][extendedAddress]", "Lager Suite")
              .append("sepa_mandate[billingAddress][firstName]", "Wilhelm")
              .append("sepa_mandate[billingAddress][lastName]", "Dix")
              .append("sepa_mandate[billingAddress][locality]", "Frankfurt")
              .append("sepa_mandate[billingAddress][postalCode]", "60001")
              .append("sepa_mandate[billingAddress][countryCodeAlpha2]", "DE")
              .append("sepa_mandate[billingAddress][region]", "Hesse");

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

    public static String generateFuturePaymentPayPalNonce(BraintreeGateway gateway) {
      QueryString payload = new QueryString();
      payload.append("paypal_account[consent_code]", "consent");

      return generatePayPalNonce(gateway, payload);
    }

    public static String generateBillingAgreementPayPalNonce(BraintreeGateway gateway) {
      QueryString payload = new QueryString();
      payload.append("paypal_account[billing_agreement_token]", "fake_ba_token");

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
      String encodedClientToken = gateway.clientToken().generate();
      String clientToken = TestHelper.decodeClientToken(encodedClientToken);
      String payload = new StringBuilder()
          .append("{\n")
            .append("\"type\": \"us_bank_account\",\n")
            .append("\"billing_address\": {\n")
                .append("\"street_address\": \"123 Ave\",\n")
                .append("\"region\": \"CA\",\n")
                .append("\"locality\": \"San Francisco\",\n")
                .append("\"postal_code\": \"94112\"\n")
            .append("},\n")
            .append("\"account_type\": \"checking\",\n")
            .append("\"routing_number\": \"123456789\",\n")
            .append("\"account_number\": \"567891234\",\n")
            .append("\"account_holder_name\": \"Dan Schulman\",\n")
            .append("\"account_description\": \"PayPal Checking - 1234\",\n")
            .append("\"ach_mandate\": {\n")
                .append("\"text\": \"\"\n")
            .append("}\n")
          .append("}")
        .toString();

        String nonce = "";
        try {
            JSONObject json = new JSONObject(clientToken);
            URL url = new URL(json.getJSONObject("braintree_api").getString("url") + "/tokens");
            SSLContext sc = SSLContext.getInstance("TLSv1.1");
            sc.init(null, null, null);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Braintree-Version", "2015-11-01");
            connection.addRequestProperty("Authorization", "Bearer integratexxxxxx_xxxxxx_xxxxxx_xxxxxx_xx1");
            connection.setDoOutput(true);
            connection.getOutputStream().write(payload.getBytes("UTF-8"));
            connection.getOutputStream().close();

            InputStream responseStream = connection.getInputStream();
            String body = StringUtils.inputStreamToString(responseStream);
            responseStream.close();
            JSONObject responseJson = new JSONObject(body);
            nonce = responseJson.getJSONObject("data").getString("id");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nonce;
    }

    public static String generateInvalidUsBankAccountNonce() {
        String[] valid_characters = "bcdfghjkmnpqrstvwxyz23456789".split("");
        String token = "tokenusbankacct";
        for(int i=0; i < 4; i++) {
            token += '_';
            for(int j=0; j<6; j++) {
               token += valid_characters[new Random().nextInt(valid_characters.length)];
            }
        }
        return token + "_xxx";
    }
}
