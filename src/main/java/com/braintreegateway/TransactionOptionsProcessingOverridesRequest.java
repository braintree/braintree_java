package com.braintreegateway;

public class TransactionOptionsProcessingOverridesRequest extends Request {
    private TransactionOptionsRequest parent;
    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;
    private String customerTaxIdentifier;

    public TransactionOptionsProcessingOverridesRequest(TransactionOptionsRequest parent) {
        this.parent = parent;
    }

    public TransactionOptionsRequest done() {
        return parent;
    }

    public TransactionOptionsProcessingOverridesRequest customerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    public TransactionOptionsProcessingOverridesRequest customerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
        return this;
    }

    public TransactionOptionsProcessingOverridesRequest customerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
        return this;
    }

    public TransactionOptionsProcessingOverridesRequest customerTaxIdentifier(String customerTaxIdentifier) {
        this.customerTaxIdentifier = customerTaxIdentifier;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("processingOverrides").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("processingOverrides");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("customerEmail", customerEmail)
            .addElement("customerFirstName", customerFirstName)
            .addElement("customerLastName", customerLastName)
            .addElement("customerTaxIdentifier", customerTaxIdentifier);

        return builder;
    }
}
