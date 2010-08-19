package com.braintreegateway;

import java.util.List;

public class SearchCriteria extends Request {
    private String xml;

    public SearchCriteria(String type, Object value) {
        this.xml = buildXMLElement(type, value);
    }

    public SearchCriteria(List<?> items) {
        StringBuilder builder = new StringBuilder();
        for (Object item : items) {
            builder.append(buildXMLElement("item", item.toString()));
        }
        this.xml = builder.toString();
    }

    @Override
    public String toXML() {
        return this.xml;
    }

    @Override
    public String toQueryString(String parent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toQueryString() {
        throw new UnsupportedOperationException();
    }
}
