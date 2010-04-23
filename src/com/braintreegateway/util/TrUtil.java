package com.braintreegateway.util;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;

public class TrUtil {
    private Configuration configuration;

    public TrUtil(Configuration configuration) {
        this.configuration = configuration;
    }

    public String buildTrData(Request request, String redirectURL) {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String dateString = String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", now);

        String trContent = new QueryString().append("api_version", Configuration.apiVersion()).append("public_key", configuration.publicKey)
                .append("redirect_url", redirectURL).append("time", dateString).toString();

        String requestQueryString = request.toQueryString();

        if (requestQueryString.length() > 0) {
            trContent += "&" + requestQueryString;
        }

        String trHash = new Crypto().hmacHash(configuration.privateKey, trContent);
        return trHash + "|" + trContent;
    }

    public boolean isValidTrQueryString(String queryString) {
        String[] pieces = queryString.split("&hash=");
        String queryStringWithoutHash = pieces[0];
        String hash = pieces[1];
        
        return hash.equals(new Crypto().hmacHash(configuration.privateKey, queryStringWithoutHash));
    }

    protected String encodeMap(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();

        String[] keys = map.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            builder.append(encodeParam(key, map.get(key)));
            if (i + 1 < keys.length) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    protected String encodeParam(String key, String value) {
        String encodedKey = "";
        String encodedValue = "";
        try {
            encodedKey = URLEncoder.encode(key, "UTF-8");
            encodedValue = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encodedKey + "=" + encodedValue;
    }
}
