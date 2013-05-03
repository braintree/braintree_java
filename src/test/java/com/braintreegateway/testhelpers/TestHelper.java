package com.braintreegateway.testhelpers;

import com.braintreegateway.*;
import com.braintreegateway.Transaction.Status;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.util.Crypto;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import org.junit.Ignore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

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
        assertEquals(trHash, new Crypto().hmacHash(configuration.privateKey, trContent));
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
}
