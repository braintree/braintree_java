package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.exceptions.NotFoundException;

public class PaymentMethodNonceGateway {
    private Http http;

    public PaymentMethodNonceGateway(Http http) {
        this.http = http;
    }

    public Result<PaymentMethodNonce> create(String paymentMethodToken) {
        NodeWrapper response = http.post("/payment_methods/" + paymentMethodToken + "/nonces");
        return parseResponse(response);
    }

    public PaymentMethodNonce find(String paymentMethodNonce) {
        NodeWrapper response = http.get("/payment_method_nonces/" + paymentMethodNonce);
        return new PaymentMethodNonce(response);
    }

    public Result<PaymentMethodNonce> parseResponse(NodeWrapper response) {
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }
}
