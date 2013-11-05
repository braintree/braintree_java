package com.braintreegateway.testhelpers;

import com.braintreegateway.util.NodeWrapper;

import java.net.URL;
import java.net.HttpURLConnection;

public class HttpHelper {

    public static int get(String urlS) throws java.net.MalformedURLException, java.io.IOException, java.net.ProtocolException {
        URL url = new URL(urlS);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }
}
