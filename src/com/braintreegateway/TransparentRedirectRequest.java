package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

public class TransparentRedirectRequest extends Request {
    private String id;

    public TransparentRedirectRequest(String queryString) {
        Map<String, String> paramMap = new HashMap<String, String>();
        String[] queryParams = queryString.split("&");

        for (String queryParam : queryParams) {
            String[] items = queryParam.split("=");
            paramMap.put(items[0], items[1]);
        }

        id = paramMap.get("id");
    }

    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append(buildXMLElement("id", id));
        return builder.toString();
    }

    public String toQueryString(String parent) {
        return null;
    }

    public String toQueryString() {
        return null;
    }
}
