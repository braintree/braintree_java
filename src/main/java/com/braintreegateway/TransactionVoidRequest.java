package com.braintreegateway;

public class TransactionVoidRequest extends Request {

    private String apiRequestKey;

    public TransactionVoidRequest apiRequestKey(String apiRequestKey) {
        this.apiRequestKey = apiRequestKey;
        return this;
    }

    @Override
    public String toQueryString() {
        return toQueryString("transaction");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    @Override
    public String toXML() {
        return buildRequest("transaction").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).addElement("api-request-key", apiRequestKey);
    }

}
