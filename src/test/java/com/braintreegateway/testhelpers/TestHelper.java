package com.braintreegateway.testhelpers;

import com.braintreegateway.*;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Sha1Hasher;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.QueryString;
import org.junit.Ignore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals(trHash, new Sha1Hasher().hmacHash(configuration.privateKey, trContent));
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

    public static void settle(BraintreeGateway gateway, String transactionId) {
        NodeWrapper response = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION).put("/transactions/" + transactionId + "/settle");
        assertTrue(response.isSuccess());
    }

    public static void escrow(BraintreeGateway gateway, String transactionId) {
        NodeWrapper response = new Http(gateway.getAuthorizationHeader(), gateway.baseMerchantURL(), Environment.DEVELOPMENT.certificateFilenames, BraintreeGateway.VERSION).put("/transactions/" + transactionId + "/escrow");
        assertTrue(response.isSuccess());
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
      ClientTokenRequest request = null;
      if (customerId != null) {
          request = new ClientTokenRequest().customerId(customerId);
      }
      String clientToken = gateway.clientToken().generate(request);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      String url = gateway.baseMerchantURL() + "/client_api/nonces.json";
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

    public static String generateOneTimePayPalNonce(BraintreeGateway gateway) {
      String clientToken = gateway.clientToken().generate(null);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      String url = gateway.baseMerchantURL() + "/client_api/v1/payment_methods/paypal_accounts";
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

    public static String generateFuturePaymentPayPalNonce(BraintreeGateway gateway) {
      String clientToken = gateway.clientToken().generate(null);

      String authorizationFingerprint = extractParamFromJson("authorizationFingerprint", clientToken);
      String url = gateway.baseMerchantURL() + "/client_api/v1/payment_methods/paypal_accounts";
      QueryString payload = new QueryString();
      payload.append("authorization_fingerprint", authorizationFingerprint).
        append("shared_customer_identifier_type", "testing").
        append("shared_customer_identifier", "test-identifier").
        append("paypal_account[consent_code]", "consent").
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
}
