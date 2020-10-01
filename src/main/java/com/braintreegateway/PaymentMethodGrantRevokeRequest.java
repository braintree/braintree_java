package com.braintreegateway;

public class PaymentMethodGrantRevokeRequest extends Request {
    private String sharedPaymentMethodToken;

    public PaymentMethodGrantRevokeRequest() {
    }

    protected PaymentMethodGrantRevokeRequest sharedPaymentMethodToken(String sharedPaymentMethodToken) {
        this.sharedPaymentMethodToken = sharedPaymentMethodToken;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("payment-method").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root)
            .addElement("shared-payment-method-token", sharedPaymentMethodToken);

        return builder;
    }
}
