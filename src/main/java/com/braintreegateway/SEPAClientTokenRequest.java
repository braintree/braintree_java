package com.braintreegateway;


/**
 * SEPA create/update endpoint is removed, new sepa transaction is no longer supported 
 */
@Deprecated
public class SEPAClientTokenRequest extends ClientTokenRequest {
    private String mandateType;
    private String mandateAcceptanceLocation;

    @Deprecated
    public SEPAClientTokenRequest mandateType(EuropeBankAccount.MandateType mandateType) {
        this.mandateType = mandateType.toString();

        return this;
    }


    @Deprecated
    public SEPAClientTokenRequest mandateAcceptanceLocation(String mandateAcceptanceLocation) {
        this.mandateAcceptanceLocation = mandateAcceptanceLocation;
        return this;
    }

    @Deprecated
    public RequestBuilder buildRequest(String root) {
        RequestBuilder builder = super.buildRequest(root);
        builder.addElement("sepaMandateType", this.mandateType);
        builder.addElement("sepaMandateAcceptanceLocation", this.mandateAcceptanceLocation);

        return builder;
    }
}
