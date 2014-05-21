package com.braintreegateway.testhelpers;

import com.braintreegateway.util.StringUtils;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.Configuration;

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class HttpHelper {

    public static int getResponseCode(String urlS) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }

    public static String get(String urlS) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return getResponseBody(connection);
    }

    public static HttpURLConnection execute(String method, String urlS, String body) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.setDoOutput(true);
        if (body != null) {
            connection.getOutputStream().write(body.getBytes("UTF-8"));
            connection.getOutputStream().close();
        }
        return connection;
    }

    public static HttpURLConnection executePost(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        return execute("POST", urlS, postBody);
    }

    public static HttpURLConnection executePut(String urlS, String putBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        return execute("PUT", urlS, putBody);
    }

    public static int postResponseCode(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        HttpURLConnection connection = executePost(urlS, postBody);
        return connection.getResponseCode();
    }

    public static String post(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        HttpURLConnection connection = executePost(urlS, postBody);
        return getResponseBody(connection);
    }

    public static String put(String url, String body) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        HttpURLConnection connection = executePut(url, body);
        return getResponseBody(connection);
    }

    private static String getResponseBody(HttpURLConnection connection) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        InputStream responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

        if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
          responseStream = new GZIPInputStream(responseStream);
        }
        String body = StringUtils.inputStreamToString(responseStream);

        responseStream.close();
        return body;
    }
}
