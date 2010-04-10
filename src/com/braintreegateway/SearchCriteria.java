package com.braintreegateway;

import java.util.List;

public class SearchCriteria {
    private String xml;

    public SearchCriteria(String type, String value) {
        this.xml = String.format("<%s>%s</%s>", type, value, type);
    }
    
    public SearchCriteria(List<?> items) {
        StringBuilder builder = new StringBuilder();
        for (Object item : items) {
            builder.append(String.format("<item>%s</item>", item.toString()));
        }
        this.xml = builder.toString();
    }

    public String toXML() {
        return this.xml;
    }
}
