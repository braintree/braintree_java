package com.braintreegateway;

public class SEPAClientTokenRequest extends ClientTokenRequest {
    private String mandateType;
    private String mandateAcceptanceLocation;

    public SEPAClientTokenRequest mandateType(SEPABankAccount.MandateType mandateType) {
        this.mandateType = mandateType.toString();

        return this;
    }

    public SEPAClientTokenRequest mandateAcceptanceLocation(String mandateAcceptanceLocation) {
        this.mandateAcceptanceLocation = mandateAcceptanceLocation;
        return this;
    }

    public RequestBuilder buildRequest(String root) {
        RequestBuilder builder = super.buildRequest(root);
        builder.addElement("sepaMandateType", this.mandateType);
        builder.addElement("sepaMandateAcceptanceLocation", this.mandateAcceptanceLocation);

        return builder;
    }
}
