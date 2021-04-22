package com.braintreegateway;

public class TaxIdentifierRequest extends Request {
    private CustomerRequest parent;
    private String countryCode;
    private String identifier;

    public TaxIdentifierRequest() {
    }

    public TaxIdentifierRequest(CustomerRequest parent) {
        this.parent = parent;
    }

    public TaxIdentifierRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public TaxIdentifierRequest identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public CustomerRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("taxIdentifier").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("taxIdentifier");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("countryCode", countryCode)
            .addElement("identifier", identifier);
    }
}
