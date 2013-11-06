package com.braintreegateway.testhelpers;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.Configuration;

import java.net.URL;
import java.net.HttpURLConnection;

public class HttpHelper {

    public static int get(String urlS) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }

    public static int post(String urlS, String postBody) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.setDoOutput(true);
        if (postBody != null) {
            connection.getOutputStream().write(postBody.getBytes("UTF-8"));
            connection.getOutputStream().close();
        }
        return connection.getResponseCode();
    }
}
