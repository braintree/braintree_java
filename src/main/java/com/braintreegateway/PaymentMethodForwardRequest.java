package com.braintreegateway;

/**
 * Provides a fluent interface to build up requests for payment method forwarding
 */
public class PaymentMethodForwardRequest extends Request {
    private String token;
    private String receivingMerchantId;

    public String getToken() {
        return token;
    }

    public String getReceivingMerchantId() {
        return receivingMerchantId;
    }

    public PaymentMethodForwardRequest token(String token) {
        this.token = token;
        return this;
    }

    public PaymentMethodForwardRequest receivingMerchantId(String receivingMerchantId) {
        this.receivingMerchantId = receivingMerchantId;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("paymentMethod").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);

        if (token != null) {
            builder.addElement("paymentMethodToken", token);
        }

        if (receivingMerchantId != null) {
            builder.addElement("receivingMerchantId", receivingMerchantId);
        }

        return builder;
    }
}
