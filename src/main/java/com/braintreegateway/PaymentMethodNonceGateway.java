package com.braintreegateway;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.exceptions.NotFoundException;

public class PaymentMethodNonceGateway {
    private Http http;
    private Configuration configuration;

    public PaymentMethodNonceGateway(Http http, Configuration configuration) {
        this.http = http;
        this.configuration = configuration;
    }

    public Result<PaymentMethodNonce> create(String paymentMethodToken) {
        NodeWrapper response = http.post(configuration.getMerchantPath() + "/payment_methods/" + paymentMethodToken + "/nonces");
        return parseResponse(response);
    }

    public Result<PaymentMethodNonce> find(String paymentMethodNonce) {
        NodeWrapper response = http.get(configuration.getMerchantPath() + "/payment_method_nonces/" + paymentMethodNonce);
        return parseResponse(response);
    }

    public Result<PaymentMethodNonce> parseResponse(NodeWrapper response) {
        return new Result<PaymentMethodNonce>(response, PaymentMethodNonce.class);
    }
}
