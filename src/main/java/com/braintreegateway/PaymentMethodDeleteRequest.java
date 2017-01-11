package com.braintreegateway;

public class PaymentMethodDeleteRequest extends Request {
    private boolean revokeAllGrants;

    public PaymentMethodDeleteRequest() {
    }

    public PaymentMethodDeleteRequest revokeAllGrants(boolean revokeAllGrants) {
        this.revokeAllGrants = revokeAllGrants;
        return this;
    }

    @Override
    public String toQueryString() {
        return buildRequest().toQueryString();
    }

    protected RequestBuilder buildRequest() {
        return new RequestBuilder("").
            addTopLevelElement("revokeAllGrants", Boolean.toString(revokeAllGrants));
    }
}
