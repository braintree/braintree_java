package com.braintreegateway;

import java.util.Map;

/**
 * Abstract class for fluent interface request builders.
 */
public abstract class Request {
    public String toXML() {
        throw new UnsupportedOperationException();
    }

    public String toQueryString(String parent) {
        throw new UnsupportedOperationException();
    }

    public String toQueryString() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> toGraphQLVariables() {
        throw new UnsupportedOperationException();
    }

    public String getKind() {
        return null;
    }

}
