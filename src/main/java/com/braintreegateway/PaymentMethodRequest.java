package com.braintreegateway;

public class PaymentMethodRequest extends Request {
    private String paymentMethodNonce;
    private String customerId;

    public PaymentMethodRequest() {
    }

    public PaymentMethodRequest paymentMethodNonce(String paymentMethodNonce) {
        this.paymentMethodNonce = paymentMethodNonce;
        return this;
    }

    public PaymentMethodRequest customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("payment-method").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("customer-id", customerId).
            addElement("payment-method-nonce", paymentMethodNonce);

        return builder;
    }
}
