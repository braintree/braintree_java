package com.braintreegateway.util;

import com.braintreegateway.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class QueryString {
    private final StringBuilder builder;

    public QueryString() {
        builder = new StringBuilder("");
    }

    public QueryString(String content) {
        builder = new StringBuilder(content);
    }

    public QueryString append(String key, Object value) {
        if (value == null) {
            return this;
        }
        if (value instanceof Request) {
            return appendRequest(key, (Request) value);
        } else if (value instanceof Map<?, ?>) {
            return appendMap(key, (Map<?,?>) value);
        }
        return appendString(key, value.toString());
    }

    public QueryString appendEncodedData(String alreadyEncodedData) {
        if (alreadyEncodedData != null && alreadyEncodedData.length() > 0) {
            builder.append('&');
            builder.append(alreadyEncodedData);
        }
        return this;
    }


    public String toString() {
        return builder.toString();
    }

    public static String encodeParam(String key, String value) {
        String encodedKey = encode(key);
        String encodedValue = encode(value);
        return encodedKey + "=" + encodedValue;
    }

    public static String DEFAULT_ENCODING = "UTF-8";

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, DEFAULT_ENCODING);
        } catch(UnsupportedEncodingException e) {
            throw new IllegalStateException(DEFAULT_ENCODING + " encoding should always be available");
        }
    }

    protected QueryString appendString(String key, String value) {
        if (key != null && !key.equals("") && value != null) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(encodeParam(key, value));
        }
        return this;
    }

    protected QueryString appendRequest(String parent, Request request) {
        if (request != null) {
            String requestQueryString = request.toQueryString(parent);
            if (requestQueryString.length() > 0) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(requestQueryString);
            }
        }
        return this;
    }

    protected QueryString appendMap(String key, Map<?, ?> value) {
        for (Object keyString : value.keySet()) {
            appendString(String.format("%s[%s]", key, keyString), value.get(keyString).toString());
        }
        return this;
    }
}
