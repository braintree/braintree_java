package com.braintreegateway.testhelpers;

import com.braintreegateway.util.StringUtils;
import com.braintreegateway.util.QueryString;
import com.braintreegateway.Configuration;

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class HttpHelper {

    public static int get(String urlS) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }

    public static HttpURLConnection executePost(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.setDoOutput(true);
        if (postBody != null) {
            connection.getOutputStream().write(postBody.getBytes("UTF-8"));
            connection.getOutputStream().close();
        }
        return connection;
    }

    public static int postResponseCode(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        HttpURLConnection connection = executePost(urlS, postBody);
        return connection.getResponseCode();
    }

    public static String post(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        HttpURLConnection connection = executePost(urlS, postBody);
        InputStream responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

        if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
          responseStream = new GZIPInputStream(responseStream);
        }
        String body = StringUtils.inputStreamToString(responseStream);

        responseStream.close();
        return body;
    }
}
