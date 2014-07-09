package com.braintreegateway;

public class PaymentMethodRequest extends Request {
    private String paymentMethodNonce;
    private String customerId;
    private String token;
    private PaymentMethodOptionsRequest optionsRequest;

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

    public PaymentMethodOptionsRequest options() {
        this.optionsRequest = new PaymentMethodOptionsRequest(this);
        return optionsRequest;
    }

    public String getToken() {
        return token;
    }

    public PaymentMethodRequest token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("payment-method").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root).
            addElement("customer-id", customerId).
            addElement("token", token).
            addElement("options", optionsRequest).
            addElement("payment-method-nonce", paymentMethodNonce);

        return builder;
    }
}
