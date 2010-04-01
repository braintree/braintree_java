package com.braintreegateway;

public class SearchCriteria {
    private String xml;

    public SearchCriteria(String type, String value) {
        this.xml = String.format("<%s>%s</%s>", type, value, type);
    }
    
    public String toXML() {
        return this.xml;
    }
}
