package com.braintreegateway;

/**
 * Abstract class for fluent interface request builders.
 */
public abstract class Request {
    public abstract String toXML();
    public abstract String toQueryString(String parent);
    public abstract String toQueryString();
    
    public String getKind() {
        return null;
    }
    
    protected String buildXMLElement(Object element) {
        return RequestBuilder.buildXMLElement(element);
    }

    protected String buildXMLElement(String name, Object element) {
        return RequestBuilder.buildXMLElement(name, element);
    }
}
