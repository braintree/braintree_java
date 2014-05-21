package com.braintreegateway;

public class SEPAClientTokenRequest extends ClientTokenRequest {
    private String mandateType;

    public SEPAClientTokenRequest mandateType(String mandateType) {
        this.mandateType = mandateType;

        return this;
    }

    public RequestBuilder buildRequest(String root) {
        RequestBuilder builder = super.buildRequest(root);
        builder.addElement("sepaMandateType", this.mandateType);

        return builder;
    }
}
